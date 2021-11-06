package xyz.e3ndr.consoleutil.ipc;

import java.io.File;
import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.file.StandardOpenOption;

import lombok.Getter;

public class MemoryMappedIpc implements IpcChannel {
    private static final int HOST_FLAG_OFFSET = 0;
    private static final int CHILD_FLAG_OFFSET = 0;
    private static final int HOST_WRITE_OFFSET = 128;
    private static final int CHILD_WRITE_OFFSET = 512;

    private static final int MAX_WRITE_SIZE = 128;

    private static class FLAGS {
        private static final int WRITE_FLAG = 0;
        private static final int READY_FLAG = 1;
    }

    private @Getter boolean isChild = false;
    private @Getter String ipcId;

    private CharBuffer charBuf;
    private FileChannel fileChannel;

    private StringBuilder recvBuffer = new StringBuilder();
    private StringBuilder sendBuffer = new StringBuilder();

    /*
     * Memory Layout:
     * 
     *   0-127   Flags
     *       0: Host Write Flag
     *       1: Host Ready Flag
     *      64: Child Write Flag
     *      65: Child Ready Flag
     *   
     * 128-511   Host Write Range
     * 512-640   Child Write Range
     * 
     * Total: 640bytes
     */

    /*
     * Example message sending from host to child:
     * 
     * Child sets ready flag to 1.
     * Host sets ready flag to 1.
     * Host writes "Hello World\0" to it's write range.
     * Host sets child's ready flag to 0.
     * Host sets the write flag to 1.
     * Child sets host's ready flag to 0.
     */

    private MemoryMappedIpc(String ipcId, boolean isChild) throws IOException {
        this.fileChannel = FileChannel.open(
            new File(String.format("%s/%s.memipc", System.getProperty("java.io.tmpdir"), ipcId)).toPath(),
            StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE
        );

        MappedByteBuffer buf = this.fileChannel.map(MapMode.READ_WRITE, 0, 4096);
        this.charBuf = buf.asCharBuffer();

        this.charBuf.put(this.getMyFlagOffset() + FLAGS.READY_FLAG, (char) 1);
    }

    private int getMyFlagOffset() {
        return this.isChild ? CHILD_FLAG_OFFSET : HOST_FLAG_OFFSET;
    }

    private int getTheirFlagOffset() {
        return this.isChild ? HOST_FLAG_OFFSET : CHILD_FLAG_OFFSET;
    }

    private int getMyWriteRange() {
        return this.isChild ? CHILD_WRITE_OFFSET : HOST_WRITE_OFFSET;
    }

    private int getTheirWriteRange() {
        return this.isChild ? HOST_WRITE_OFFSET : CHILD_WRITE_OFFSET;
    }

    private boolean isWriteLocked() {
        boolean myWriteFlag = this.charBuf.get(this.getMyFlagOffset() + FLAGS.WRITE_FLAG) > 0;
        boolean theirReadyFlag = this.charBuf.get(this.getTheirFlagOffset() + FLAGS.READY_FLAG) > 0;

        return !myWriteFlag && !theirReadyFlag;
    }

    private boolean shouldRead() {
        boolean theirWriteFlag = this.charBuf.get(this.getTheirFlagOffset() + FLAGS.WRITE_FLAG) > 0;
        boolean myReadyFlag = this.charBuf.get(this.getMyFlagOffset() + FLAGS.READY_FLAG) > 0;

        return !theirWriteFlag && !myReadyFlag;
    }

    @Override
    public void write(String str) throws IOException, InterruptedException {
        this.sendBuffer = new StringBuilder(str).append('\0');

        this.charBuf.put(this.getMyFlagOffset() + FLAGS.READY_FLAG, '\0');

        do {
            while (this.isWriteLocked() || (this.recvBuffer.length() > 0)) {
                Thread.sleep(100);
            }

            String sub = this.sendBuffer.substring(0, Math.min(MAX_WRITE_SIZE - 1, this.sendBuffer.length()));
            this.sendBuffer.delete(0, sub.length());

            this.charBuf.position(this.getMyWriteRange());
            this.charBuf.put(sub);

            this.charBuf.put(this.getTheirFlagOffset() + FLAGS.READY_FLAG, '\0');
            this.charBuf.put(this.getMyFlagOffset() + FLAGS.WRITE_FLAG, '\1');
        } while (this.sendBuffer.length() > 0);

        this.charBuf.put(this.getMyFlagOffset() + FLAGS.READY_FLAG, '\1');
    }

    @Override
    public String read() throws IOException, InterruptedException {
        boolean isReading = true;
        while (isReading) {
            while (!this.shouldRead()) {
                Thread.sleep(100);
            }

            char[] buf = new char[MAX_WRITE_SIZE];
            this.charBuf.position(this.getTheirWriteRange());
            this.charBuf.get(buf, 0, MAX_WRITE_SIZE);

            int i = 0;
            while (i < MAX_WRITE_SIZE) {
                if (buf[i] == 0) {
                    isReading = false;
                    break;
                }
                i++;
            }

            String content = new String(buf);
            this.recvBuffer.append(content.substring(i));

            System.out.println(content);
        }

        String result = this.recvBuffer.toString();
        this.recvBuffer.setLength(0);
        return result;
    }

    @Override
    public void close() throws IOException {
        this.fileChannel.close();
    }

    public static MemoryMappedIpc startHostIpc(String ipcId) throws IOException, InterruptedException {
        return new MemoryMappedIpc(ipcId, true);
    }

    public static MemoryMappedIpc startChildIpc(String ipcId) throws IOException, InterruptedException {
        return new MemoryMappedIpc(ipcId, true);
    }

}
