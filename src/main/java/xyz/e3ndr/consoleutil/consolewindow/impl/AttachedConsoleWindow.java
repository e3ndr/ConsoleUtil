package xyz.e3ndr.consoleutil.consolewindow.impl;

import java.awt.Dimension;
import java.io.IOException;

import org.jetbrains.annotations.Nullable;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;
import xyz.e3ndr.consoleutil.ConsoleUtil;
import xyz.e3ndr.consoleutil.ansi.AnsiCommand;
import xyz.e3ndr.consoleutil.ansi.ConsoleAttribute;
import xyz.e3ndr.consoleutil.ansi.ConsoleColor;
import xyz.e3ndr.consoleutil.consolewindow.BarStyle;
import xyz.e3ndr.consoleutil.consolewindow.ConsoleWindow;
import xyz.e3ndr.consoleutil.std.StdStreams;

@Accessors(chain = true)
public class AttachedConsoleWindow implements ConsoleWindow {
    private StringBuilder buffer = new StringBuilder();
    private @Getter @Setter StdStreams std = StdStreams.CURRENT;

    private @Getter @Setter boolean autoFlushing = true;

    public AttachedConsoleWindow() {
        if (ConsoleUtil.getAttachedConsoleWindow() != null) {
            throw new IllegalStateException("Already attached.");
        }
    }

    private void writeCommand(AnsiCommand command, Object... format) {
        this.writeOut(String.format(command.getSequence(), format));
    }

    private void writeOut(String str) {
        if (this.autoFlushing) {
            this.std.out.print(str);
        } else {
            this.buffer.append(str);
        }
    }

    @Override
    public ConsoleWindow cursorTo(int x, int y) {
        this.writeCommand(AnsiCommand.CURSOR_MOVE, y + 1, x + 1);

        return this;
    }

    @Override
    public ConsoleWindow cursorUp(int amount) {
        this.writeCommand(AnsiCommand.CURSOR_UP, amount);

        return this;
    }

    @Override
    public ConsoleWindow cursorDown(int amount) {
        this.writeCommand(AnsiCommand.CURSOR_DOWN, amount);

        return this;
    }

    @Override
    public ConsoleWindow cursorLeft(int amount) {
        this.writeCommand(AnsiCommand.CURSOR_LEFT, amount);

        return this;
    }

    @Override
    public ConsoleWindow cursorRight(int amount) {
        this.writeCommand(AnsiCommand.CURSOR_RIGHT, amount);

        return this;
    }

    @Override
    public ConsoleWindow saveCursorPosition() {
        this.writeCommand(AnsiCommand.CURSOR_SAVE);

        return this;
    }

    @Override
    public ConsoleWindow restoreCursorPosition() {
        this.writeCommand(AnsiCommand.CURSOR_RESTORE);

        return this;
    }

    @Override
    public ConsoleWindow clearScreen() {
        this.writeCommand(AnsiCommand.ERASE_DISPLAY);

        return this;
    }

    @Override
    public ConsoleWindow write(@Nullable Object format, @Nullable Object... args) {
        String line = Formatting.parseFormat(format, args);

        this.writeOut(line);

        return this;
    }

    @Override
    public ConsoleWindow writeAt(int x, int y, @Nullable Object format, @Nullable Object... args) {
        String line = Formatting.parseFormat(format, args);

        this.saveCursorPosition();
        this.cursorTo(x, y);
        this.writeOut(line);
        this.restoreCursorPosition();

        return this;
    }

    @Override
    public ConsoleWindow loadingBar(@NonNull BarStyle style, double progress, int size, boolean showPercent) {
        this.writeOut(style.format(progress, size, showPercent));

        return this;
    }

    @Override
    public ConsoleWindow loadingBarAt(int x, int y, @NonNull BarStyle style, double progress, int size, boolean showPercent) {
        this.saveCursorPosition();
        this.cursorTo(x, y);
        this.writeOut(style.format(progress, size, showPercent));
        this.restoreCursorPosition();

        return this;
    }

    @Override
    public ConsoleWindow replaceLine(@Nullable Object format, @Nullable Object... args) {
        String line = Formatting.parseFormat(format, args);

        this.writeCommand(AnsiCommand.ERASE_LINE);
        this.writeOut(line);

        return this;
    }

    @Override
    public ConsoleWindow replaceLineAt(int y, @Nullable Object format, @Nullable Object... args) {
        String line = Formatting.parseFormat(format, args);

        this.saveCursorPosition();
        this.cursorTo(0, y);
        this.writeCommand(AnsiCommand.ERASE_LINE);
        this.writeOut(line);
        this.restoreCursorPosition();

        return this;
    }

    @Override
    public ConsoleWindow setTextColor(@NonNull ConsoleColor color) {
        this.writeOut(color.getForeground());

        return this;
    }

    @Override
    public ConsoleWindow setBackgroundColor(@NonNull ConsoleColor color) {
        this.writeOut(color.getBackground());

        return this;
    }

    @Override
    public ConsoleWindow clearLine() {
        this.writeCommand(AnsiCommand.ERASE_LINE);

        return this;
    }

    @Override
    public ConsoleWindow clearLine(int y) {
        this.saveCursorPosition();
        this.cursorTo(0, y);
        this.clearLine();
        this.restoreCursorPosition();

        return this;
    }

    @Override
    public ConsoleWindow setAttributes(@NonNull ConsoleAttribute... attributes) {
        for (ConsoleAttribute attribute : attributes) {
            this.writeOut(attribute.getAnsi());
        }

        return this;
    }

    @Override
    public ConsoleWindow update() {
        this.std.out.print(this.buffer);
        this.std.out.flush();
        this.buffer.setLength(0);

        return this;
    }

    @Override
    public ConsoleWindow clearBuffer() {
        this.buffer.setLength(0);

        return this;
    }

    @Override
    public void close() throws IOException {}

    @Override
    public ConsoleWindow bell() {
        ConsoleUtil.bell();
        return this;
    }

    @Override
    public ConsoleWindow setSize(int width, int height) throws IOException, InterruptedException {
        ConsoleUtil.setSize(width, height);
        return this;
    }

    @Override
    public Dimension getSize() throws IOException, InterruptedException {
        return ConsoleUtil.getSize();
    }

    @Override
    public ConsoleWindow setTitle(String title) throws IOException, InterruptedException {
        ConsoleUtil.setTitle(title);
        return this;
    }

}
