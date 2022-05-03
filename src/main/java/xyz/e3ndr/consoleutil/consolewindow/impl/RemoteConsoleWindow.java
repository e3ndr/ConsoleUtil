package xyz.e3ndr.consoleutil.consolewindow.impl;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.concurrent.TimeUnit;

import org.jetbrains.annotations.Nullable;

import co.casterlabs.rakurai.json.Rson;
import co.casterlabs.rakurai.json.element.JsonArray;
import co.casterlabs.rakurai.json.element.JsonObject;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import xyz.e3ndr.consoleutil.ConsoleUtil;
import xyz.e3ndr.consoleutil.ansi.ConsoleAttribute;
import xyz.e3ndr.consoleutil.ansi.ConsoleColor;
import xyz.e3ndr.consoleutil.consolewindow.BarStyle;
import xyz.e3ndr.consoleutil.consolewindow.ConsoleWindow;
import xyz.e3ndr.consoleutil.ipc.IpcChannel;
import xyz.e3ndr.consoleutil.ipc.MemoryMappedIpc;

public class RemoteConsoleWindow implements ConsoleWindow {
    private static final long TIMEOUT = TimeUnit.SECONDS.toMillis(1);

    private boolean autoFlushing = true;
    private boolean closed = false;

    private long lastPing = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(5); // Give it some lee-way

    private @Getter IpcChannel ipcChannel;

    public RemoteConsoleWindow(String ipcId) throws IOException, InterruptedException {
        startWindow(ipcId);

        this.ipcChannel = MemoryMappedIpc.startHostIpc(ipcId);

        Thread readThread = new Thread(() -> {
            while (!this.closed) {
                try {
                    String line = this.ipcChannel.read();

                    if (line.equals("PING")) {
                        this.lastPing = System.currentTimeMillis();
                    } else {
                        JsonObject command = Rson.DEFAULT.fromJson(line, JsonObject.class);

                        this.parseCommand(command);
                    }
                } catch (Exception e) {}
            }
        });

        readThread.setDaemon(true);
        readThread.setName("Remote Console Window (ipcId = " + ipcId + ")");
        readThread.start();

        Thread keepAliveWatchdogThread = new Thread(() -> {
            while (!this.closed) {
                try {
                    Thread.sleep(500);

                    if ((System.currentTimeMillis() - this.lastPing) > TIMEOUT) {
                        // Other end has died.
                        this.ipcChannel.close();
                    }
                } catch (Exception e) {}
            }
        });

        keepAliveWatchdogThread.setDaemon(true);
        keepAliveWatchdogThread.setName("Remote Console Window (ipcId = " + ipcId + ")");
        keepAliveWatchdogThread.start();

        Thread keepAliveThread = new Thread(() -> {
            while (!this.closed) {
                try {
                    Thread.sleep(500);

                    this.ipcChannel.write("PING");
                } catch (Exception e) {}
            }
        });

        keepAliveThread.setDaemon(true);
        keepAliveThread.setName("Remote Console Window (ipcId = " + ipcId + ")");
        keepAliveThread.start();
    }

    @SneakyThrows
    private void parseCommand(JsonObject command) {
        switch (command.getString("type")) {
            // TODO
        }
    }

    @SneakyThrows
    private void safeIpcWrite(JsonObject packet) {
        if (this.closed) {
            throw new RuntimeException("ConsoleWindow is closed.");
        }

        this.ipcChannel.write(packet.toString());
    }

    @Override
    public ConsoleWindow cursorTo(int x, int y) {
        this.safeIpcWrite(
            new JsonObject()
                .put("method", "cursorTo(int,int)")
                .put("x", x)
                .put("y", y)
        );
        return this;
    }

    @Override
    public ConsoleWindow cursorUp(int amount) {
        this.safeIpcWrite(
            new JsonObject()
                .put("method", "cursorUp(int)")
                .put("amount", amount)
        );
        return this;
    }

    @Override
    public ConsoleWindow cursorDown(int amount) {
        this.safeIpcWrite(
            new JsonObject()
                .put("method", "cursorDown(int)")
                .put("amount", amount)
        );
        return this;
    }

    @Override
    public ConsoleWindow cursorLeft(int amount) {
        this.safeIpcWrite(
            new JsonObject()
                .put("method", "cursorLeft(int)")
                .put("amount", amount)
        );
        return this;
    }

    @Override
    public ConsoleWindow cursorRight(int amount) {
        this.safeIpcWrite(
            new JsonObject()
                .put("method", "cursorRight(int)")
                .put("amount", amount)
        );
        return this;
    }

    @Override
    public ConsoleWindow saveCursorPosition() {
        this.safeIpcWrite(
            new JsonObject()
                .put("method", "saveCursorPosition()")
        );
        return this;
    }

    @Override
    public ConsoleWindow restoreCursorPosition() {
        this.safeIpcWrite(
            new JsonObject()
                .put("method", "restoreCursorPosition()")
        );
        return this;
    }

    @Override
    public ConsoleWindow clearScreen() {
        this.safeIpcWrite(
            new JsonObject()
                .put("method", "clearScreen()")
        );
        return this;
    }

    @Override
    public ConsoleWindow write(@Nullable Object format, @Nullable Object... args) {
        String line = Formatting.parseFormat(format, args);
        this.safeIpcWrite(
            new JsonObject()
                .put("method", "write(LINE)")
                .put("line", line)
        );
        return this;
    }

    @Override
    public ConsoleWindow writeAt(int x, int y, @Nullable Object format, @Nullable Object... args) {
        String line = Formatting.parseFormat(format, args);
        this.safeIpcWrite(
            new JsonObject()
                .put("method", "writeAt(int,int,LINE)")
                .put("x", x)
                .put("y", y)
                .put("line", line)
        );
        return this;
    }

    @SuppressWarnings("deprecation")
    @Override
    public ConsoleWindow loadingBar(@NonNull BarStyle style, double progress, int size, boolean showPercent) {
        this.safeIpcWrite(
            new JsonObject()
                .put("method", "loadingBar(BarStyle,double,int,boolean)")
                .put("style", style.serialize())
                .put("progress", progress)
                .put("size", size)
                .put("showPercent", showPercent)
        );
        return this;
    }

    @SuppressWarnings("deprecation")
    @Override
    public ConsoleWindow loadingBarAt(int x, int y, @NonNull BarStyle style, double progress, int size, boolean showPercent) {
        this.safeIpcWrite(
            new JsonObject()
                .put("method", "loadingBarAt(int,int,BarStyle,double,int,boolean)")
                .put("x", x)
                .put("y", y)
                .put("style", style.serialize())
                .put("progress", progress)
                .put("size", size)
                .put("showPercent", showPercent)
        );
        return this;
    }

    @Override
    public ConsoleWindow replaceLine(@Nullable Object format, @Nullable Object... args) {
        String line = Formatting.parseFormat(format, args);
        this.safeIpcWrite(
            new JsonObject()
                .put("method", "replaceLine(LINE)")
                .put("line", line)
        );
        return this;
    }

    @Override
    public ConsoleWindow replaceLineAt(int y, @Nullable Object format, @Nullable Object... args) {
        String line = Formatting.parseFormat(format, args);
        this.safeIpcWrite(
            new JsonObject()
                .put("method", "replaceLineAt(int,LINE)")
                .put("y", y)
                .put("line", line)
        );
        return this;
    }

    @Override
    public ConsoleWindow setTextColor(@NonNull ConsoleColor color) {
        this.safeIpcWrite(
            new JsonObject()
                .put("method", "setTextColor(ConsoleColor)")
                .put("color", color.name())
        );
        return this;
    }

    @Override
    public ConsoleWindow setBackgroundColor(@NonNull ConsoleColor color) {
        this.safeIpcWrite(
            new JsonObject()
                .put("method", "setBackgroundColor(ConsoleColor)")
                .put("color", color.name())
        );
        return this;
    }

    @Override
    public ConsoleWindow clearLine() {
        this.safeIpcWrite(
            new JsonObject()
                .put("method", "clearLine()")
        );
        return this;
    }

    @Override
    public ConsoleWindow clearLine(int y) {
        this.safeIpcWrite(
            new JsonObject()
                .put("method", "clearLine(int)")
                .put("y", y)
        );
        return this;
    }

    @Override
    public ConsoleWindow setAttributes(@NonNull ConsoleAttribute... attributes) {
        JsonArray attributesArr = new JsonArray();

        for (ConsoleAttribute attr : attributes) {
            attributesArr.add(attr.name());
        }

        this.safeIpcWrite(
            new JsonObject()
                .put("method", "setAttributes(ConsoleAttribute[])")
                .put("attributes", attributesArr)
        );
        return this;
    }

    @Override
    public ConsoleWindow flush() {
        this.safeIpcWrite(
            new JsonObject()
                .put("method", "flush()")
        );
        return this;
    }

    @Override
    public ConsoleWindow clearBuffer() {
        this.safeIpcWrite(
            new JsonObject()
                .put("method", "clearBuffer()")
        );
        return this;
    }

    @Override
    public ConsoleWindow setAutoFlushing(boolean autoFlushing) {
        this.autoFlushing = autoFlushing;
        this.safeIpcWrite(
            new JsonObject()
                .put("method", "setAutoFlushing(boolean)")
                .put("autoFlushing", autoFlushing)
        );
        return this;
    }

    @Override
    public boolean isAutoFlushing() {
        return this.autoFlushing;
    }

    @Override
    public ConsoleWindow bell() {
        this.safeIpcWrite(
            new JsonObject()
                .put("method", "bell()")
        );
        return this;
    }

    @Override
    public ConsoleWindow setSize(int width, int height) throws IOException, InterruptedException {
        this.safeIpcWrite(
            new JsonObject()
                .put("method", "setSize(int,int)")
                .put("width", width)
                .put("height", height)
        );
        return this;
    }

    @Override
    public ConsoleWindow setTitle(String title) throws IOException, InterruptedException {
        this.safeIpcWrite(
            new JsonObject()
                .put("method", "setTitle(String)")
                .put("title", title)
        );
        return this;
    }

    @Override
    public void close() throws IOException {
        this.safeIpcWrite(
            new JsonObject()
                .put("method", "close()")
        );

        this.closed = true;
        this.ipcChannel.close();
    }

    @Override
    public Dimension getSize() throws IOException, InterruptedException {
        throw new UnsupportedOperationException();
    }

    // Open the other window.
    private static void startWindow(String ipcId) throws IOException, InterruptedException {
        String jvmArgs = String.join(" ", ManagementFactory.getRuntimeMXBean().getInputArguments());
        String entry = System.getProperty("sun.java.command"); // Tested, present in OpenJDK and Oracle
        String classpath = System.getProperty("java.class.path");
        String javaHome = System.getProperty("java.home");

        String[] args = entry.split(" ");
        File entryFile = new File(args[0]);

        if (entryFile.exists()) { // If the entry is a file, not a main method.
            ConsoleUtil.startConsoleWindow(
                String.format(
                    "\"%s/bin/java\" -DStartedWithConsole=true %s -cp \"%s,%s\" xyz.e3ndr.consoleutil.consolewindow.impl.RemoteConsoleWindowLauncherInstance %s",
                    javaHome,
                    jvmArgs,
                    classpath,
                    entryFile.getCanonicalPath(),
                    ipcId
                )
            );
        } else {
            ConsoleUtil.startConsoleWindow(
                String.format(
                    "\"%s/bin/java\" -DStartedWithConsole=true %s -cp \"%s\" xyz.e3ndr.consoleutil.consolewindow.impl.RemoteConsoleWindowLauncherInstance %s",
                    javaHome,
                    jvmArgs,
                    classpath,
                    ipcId
                )
            );
        }
    }

}
