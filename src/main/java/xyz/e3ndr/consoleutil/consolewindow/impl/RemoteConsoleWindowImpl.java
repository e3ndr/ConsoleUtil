package xyz.e3ndr.consoleutil.consolewindow.impl;

import java.io.IOException;

import org.jetbrains.annotations.Nullable;

import co.casterlabs.rakurai.json.element.JsonArray;
import co.casterlabs.rakurai.json.element.JsonObject;
import lombok.NonNull;
import lombok.SneakyThrows;
import xyz.e3ndr.consoleutil.ansi.ConsoleAttribute;
import xyz.e3ndr.consoleutil.ansi.ConsoleColor;
import xyz.e3ndr.consoleutil.consolewindow.BarStyle;
import xyz.e3ndr.consoleutil.consolewindow.ConsoleWindow;
import xyz.e3ndr.consoleutil.ipc.IpcChannel;
import xyz.e3ndr.consoleutil.ipc.MemoryMappedIpc;
import xyz.e3ndr.fastloggingframework.logging.LoggingUtil;

public class RemoteConsoleWindowImpl implements ConsoleWindow {
    private boolean autoFlushing = true;

    private IpcChannel ipcChannel;

    public RemoteConsoleWindowImpl(String ipcId) throws IOException, InterruptedException {
        this.ipcChannel = MemoryMappedIpc.startHostIpc(ipcId);
    }

    @SneakyThrows
    private void safeIpcWrite(JsonObject packet) {
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
        String line = LoggingUtil.parseFormat(format, args);
        this.safeIpcWrite(
            new JsonObject()
                .put("method", "write(LINE)")
                .put("line", line)
        );
        return this;
    }

    @Override
    public ConsoleWindow writeAt(int x, int y, @Nullable Object format, @Nullable Object... args) {
        String line = LoggingUtil.parseFormat(format, args);
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
                .put("method", "loadingBar(int,int,BarStyle,double,int,boolean)")
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
        String line = LoggingUtil.parseFormat(format, args);
        this.safeIpcWrite(
            new JsonObject()
                .put("method", "replaceLine(LINE)")
                .put("line", line)
        );
        return this;
    }

    @Override
    public ConsoleWindow replaceLineAt(int y, @Nullable Object format, @Nullable Object... args) {
        String line = LoggingUtil.parseFormat(format, args);
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
                .put("color", "setTextColor(ConsoleColor)")
                .put("amount", color.name())
        );
        return this;
    }

    @Override
    public ConsoleWindow setBackgroundColor(@NonNull ConsoleColor color) {
        this.safeIpcWrite(
            new JsonObject()
                .put("method", "setTextColor(ConsoleColor)")
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
    public ConsoleWindow update() {
        this.safeIpcWrite(
            new JsonObject()
                .put("method", "update()")
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

}
