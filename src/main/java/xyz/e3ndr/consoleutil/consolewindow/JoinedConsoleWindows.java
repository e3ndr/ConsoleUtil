package xyz.e3ndr.consoleutil.consolewindow;

import java.awt.Dimension;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import lombok.NonNull;
import xyz.e3ndr.consoleutil.ansi.ConsoleAttribute;
import xyz.e3ndr.consoleutil.ansi.ConsoleColor;

/**
 * JoinedConsoleWindows allows you to update multiple windows at once with the
 * same information.
 */
public class JoinedConsoleWindows implements ConsoleWindow {
    private List<ConsoleWindow> windows = new ArrayList<>();
    private boolean autoFlushing = true;

    /**
     * Adds the specified window to the list.
     *
     * @param  additionalWindow the additional window
     * 
     * @return                  this instance, for chaining
     */
    public JoinedConsoleWindows add(@NonNull ConsoleWindow additionalWindow) {
        this.windows.add(additionalWindow);
        additionalWindow.setAutoFlushing(this.autoFlushing);
        return this;
    }

    /**
     * Removes the specified window from the list.
     *
     * @param  additionalWindow the additional window
     * 
     * @return                  this instance, for chaining
     */
    public JoinedConsoleWindows remove(@NonNull ConsoleWindow additionalWindow) {
        this.windows.remove(additionalWindow);
        return this;
    }

    @Override
    public ConsoleWindow cursorTo(int x, int y) {
        this.windows.forEach((w) -> w.cursorTo(x, y));
        return this;
    }

    @Override
    public ConsoleWindow cursorUp(int amount) {
        this.windows.forEach((w) -> w.cursorUp(amount));
        return this;
    }

    @Override
    public ConsoleWindow cursorDown(int amount) {
        this.windows.forEach((w) -> w.cursorDown(amount));
        return this;
    }

    @Override
    public ConsoleWindow cursorLeft(int amount) {
        this.windows.forEach((w) -> w.cursorLeft(amount));
        return this;
    }

    @Override
    public ConsoleWindow cursorRight(int amount) {
        this.windows.forEach((w) -> w.cursorRight(amount));
        return this;
    }

    @Override
    public ConsoleWindow saveCursorPosition() {
        this.windows.forEach((w) -> w.saveCursorPosition());
        return this;
    }

    @Override
    public ConsoleWindow restoreCursorPosition() {
        this.windows.forEach((w) -> w.restoreCursorPosition());
        return this;
    }

    @Override
    public ConsoleWindow clearScreen() {
        this.windows.forEach((w) -> w.clearScreen());
        return this;
    }

    @Override
    public ConsoleWindow write(@Nullable Object format, @Nullable Object... args) {
        this.windows.forEach((w) -> w.write(format, args));
        return this;
    }

    @Override
    public ConsoleWindow writeAt(int x, int y, @Nullable Object format, @Nullable Object... args) {
        this.windows.forEach((w) -> w.writeAt(x, y, format, args));
        return this;
    }

    @Override
    public ConsoleWindow loadingBar(@NonNull BarStyle style, double progress, int size, boolean showPercent) {
        this.windows.forEach((w) -> w.loadingBar(style, progress, size, showPercent));
        return this;
    }

    @Override
    public ConsoleWindow loadingBarAt(int x, int y, @NonNull BarStyle style, double progress, int size, boolean showPercent) {
        this.windows.forEach((w) -> w.loadingBarAt(x, y, style, progress, size, showPercent));
        return this;
    }

    @Override
    public ConsoleWindow replaceLine(@Nullable Object format, @Nullable Object... args) {
        this.windows.forEach((w) -> w.replaceLine(format, args));
        return this;
    }

    @Override
    public ConsoleWindow replaceLineAt(int y, @Nullable Object format, @Nullable Object... args) {
        this.windows.forEach((w) -> w.replaceLineAt(y, format, args));
        return this;
    }

    @Override
    public ConsoleWindow setTextColor(@NonNull ConsoleColor color) {
        this.windows.forEach((w) -> w.setTextColor(color));
        return this;
    }

    @Override
    public ConsoleWindow setBackgroundColor(@NonNull ConsoleColor color) {
        this.windows.forEach((w) -> w.setBackgroundColor(color));
        return this;
    }

    @Override
    public ConsoleWindow clearLine() {
        this.windows.forEach((w) -> w.clearLine());
        return this;
    }

    @Override
    public ConsoleWindow clearLine(int y) {
        this.windows.forEach((w) -> w.clearLine(y));
        return this;
    }

    @Override
    public ConsoleWindow setAttributes(@NonNull ConsoleAttribute... attributes) {
        this.windows.forEach((w) -> w.setAttributes(attributes));
        return this;
    }

    @Override
    public ConsoleWindow flush() {
        this.windows.forEach((w) -> w.flush());
        return this;
    }

    @Override
    public ConsoleWindow clearBuffer() {
        this.windows.forEach((w) -> w.clearBuffer());
        return this;
    }

    @Override
    public ConsoleWindow setAutoFlushing(boolean autoFlushing) {
        this.autoFlushing = autoFlushing;
        this.windows.forEach((w) -> w.setAutoFlushing(this.autoFlushing));
        return this;
    }

    @Override
    public boolean isAutoFlushing() {
        return this.autoFlushing;
    }

    /**
     * Closes all windows, if an IOException occurs then the remaining windows may
     * not be closed.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Override
    public void close() throws IOException {
        for (ConsoleWindow w : this.windows) {
            w.close();
        }
    }

    @Override
    public ConsoleWindow bell() {
        this.windows.forEach((w) -> w.bell());
        return this;
    }

    @Override
    public ConsoleWindow setSize(int width, int height) throws IOException, InterruptedException {
        for (ConsoleWindow w : this.windows) {
            w.setSize(width, height);
        }
        return this;
    }

    @Override
    public Dimension getSize() throws IOException, InterruptedException {
        throw new IOException("The result of this operation would not be accurate and has thus not been implemented.");
    }

    @Override
    public ConsoleWindow setTitle(String title) throws IOException, InterruptedException {
        for (ConsoleWindow w : this.windows) {
            w.setTitle(title);
        }
        return this;
    }

}
