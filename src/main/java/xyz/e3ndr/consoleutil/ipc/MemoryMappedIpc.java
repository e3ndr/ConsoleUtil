package xyz.e3ndr.consoleutil.ipc;

import java.io.File;
import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.file.StandardOpenOption;

import lombok.Getter;
import xyz.e3ndr.fastloggingframework.logging.FastLogger;

public class MemoryMappedIpc implements IpcChannel {
    private static final long WAIT_TIME = 50;

    private static final int SIZE = 1024;

    private static class FLAGS {
        private static final int READY_FLAG = 0;
    }

    private @Getter boolean isChild = false;
    private @Getter String ipcId;

    private FileChannel sendFileChannel;
    private CharBuffer sendCharBuf;
    private FileChannel recvFileChannel;
    private CharBuffer recvCharBuf;

    private StringBuilder recvBuffer = new StringBuilder();
    private StringBuilder sendBuffer = new StringBuilder();

    private boolean closed = false;

    private boolean busyWriting = false;
    private boolean busyReading = false;

    private FastLogger logger;

    private MemoryMappedIpc(String ipcId, boolean isChild) throws IOException {
        String sendFile;
        String recvFile;

        if (isChild) {
            // Flip them.
            recvFile = String.format("%s-1.memipc", ipcId);
            sendFile = String.format("%s-2.memipc", ipcId);
            this.logger = new FastLogger(String.format("Child IPC (%s)", ipcId));
        } else {
            sendFile = String.format("%s-1.memipc", ipcId);
            recvFile = String.format("%s-2.memipc", ipcId);
            this.logger = new FastLogger(String.format("Host IPC (%s)", ipcId));
        }

        this.logger.debug("My send file: %s", sendFile);
        this.logger.debug("My recv file: %s", recvFile);

        long bufSizeInBytes = (SIZE + 1) * Character.BYTES;

        this.sendFileChannel = FileChannel.open(new File(System.getProperty("java.io.tmpdir"), sendFile).toPath(), StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE);
        this.sendCharBuf = this.sendFileChannel.map(MapMode.READ_WRITE, 0, bufSizeInBytes).asCharBuffer();

        this.recvFileChannel = FileChannel.open(new File(System.getProperty("java.io.tmpdir"), recvFile).toPath(), StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE);
        this.recvCharBuf = this.recvFileChannel.map(MapMode.READ_WRITE, 0, bufSizeInBytes).asCharBuffer();

        if (!isChild) {
            char[] empty = new char[SIZE];
            this.sendCharBuf.put(empty);
            this.recvCharBuf.put(empty);
        }

        this.sendCharBuf.put(0, (char) 1);
    }

    private void waitToWrite() throws InterruptedException {
        while (this.sendCharBuf.get(FLAGS.READY_FLAG) != 0) {
            Thread.sleep(WAIT_TIME);
            this.logger.debug("Not ready to write.");

            if (this.closed) {
                throw new InterruptedException("IPC channel was closed.");
            }
        }
        this.logger.debug("Ready to write.");
    }

    private void waitToRead() throws InterruptedException {
        while (this.recvCharBuf.get(FLAGS.READY_FLAG) != 1) {
            Thread.sleep(WAIT_TIME);
            this.logger.debug("Not ready to read.");

            if (this.closed) {
                throw new InterruptedException("IPC channel was closed.");
            }
        }
        this.logger.debug("Ready to read.");
    }

    @Override
    public void write(String str) throws IOException, InterruptedException {
        try {
            synchronized (this.sendBuffer) {
                while (this.busyWriting) {
                    this.sendBuffer.wait();
                }
            }

            this.busyWriting = true;

            this.sendBuffer = new StringBuilder(str).append('\0');

            do {
                this.waitToWrite();

                String sub = this.sendBuffer.substring(0, Math.min(SIZE, this.sendBuffer.length()));
                this.sendBuffer.delete(0, sub.length());

                this.sendCharBuf.position(1);
                this.sendCharBuf.put(sub);

                // Signal that there's data to be read.
                this.sendCharBuf.put(FLAGS.READY_FLAG, (char) 1);
            } while (this.sendBuffer.length() > 0);

            this.logger.debug("Finished sending string.");
        } finally {
            synchronized (this.sendBuffer) {
                this.busyWriting = false;
                this.sendBuffer.notify();
            }
        }
    }

    @Override
    public String read() throws IOException, InterruptedException {
        try {
            synchronized (this.recvCharBuf) {
                while (this.busyReading) {
                    this.recvCharBuf.wait();
                }
            }

            this.busyReading = true;

            boolean isReading = true;

            while (isReading) {
                // Ready to read more.
                this.recvCharBuf.put(FLAGS.READY_FLAG, (char) 0);

                this.waitToRead();

                char[] read = new char[SIZE];
                this.recvCharBuf.position(1);
                this.recvCharBuf.get(read);

                int contentLen = 0;
                while (contentLen < SIZE) {
                    if (read[contentLen] == 0) {
                        isReading = false;
                        this.logger.debug("Reached the end of the string.");
                        break;
                    }
                    contentLen++;
                }

                String content = new String(read).substring(0, contentLen);
                this.recvBuffer.append(content);
            }

            // No longer ready to read.
            this.recvCharBuf.put(FLAGS.READY_FLAG, (char) 1);

            String result = this.recvBuffer.toString();
            this.recvBuffer.setLength(0);

            return result;
        } finally {
            synchronized (this.recvCharBuf) {
                this.busyReading = false;
                this.recvCharBuf.notify();
            }
        }
    }

    @Override
    public void close() throws IOException {
        this.closed = true;
        this.sendFileChannel.close();
        this.recvFileChannel.close();
    }

    public static MemoryMappedIpc startHostIpc(String ipcId) throws IOException, InterruptedException {
        return new MemoryMappedIpc(ipcId, false);
    }

    public static MemoryMappedIpc startChildIpc(String ipcId) throws IOException, InterruptedException {
        return new MemoryMappedIpc(ipcId, true);
    }

}
