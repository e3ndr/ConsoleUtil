package xyz.e3ndr.consoleutil;

import java.io.IOException;

import org.jetbrains.annotations.Nullable;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;
import xyz.e3ndr.consoleutil.ansi.AnsiCommand;
import xyz.e3ndr.consoleutil.ansi.ConsoleAttribute;
import xyz.e3ndr.consoleutil.ansi.ConsoleColor;
import xyz.e3ndr.fastloggingframework.logging.LoggingUtil;

@Accessors(chain = true)
public class ConsoleWindow {
    private StringBuilder buffer = new StringBuilder();
    private @Getter @Setter boolean autoFlushing = true;

    /**
     * Instantiates a new console window.
     *
     * @throws IOException          Signals that an I/O exception has occurred.
     * @throws InterruptedException the interrupted exception
     */
    public ConsoleWindow() throws IOException, InterruptedException {
        ConsoleUtil.clearConsole();

        ConsoleUtil.getJLine().setEchoEnabled(false);
    }

    private void writeCommand(AnsiCommand command, Object... format) {
        this.writeOut(String.format(command.getSequence(), format));
    }

    private void writeOut(String str) {
        if (this.autoFlushing) {
            System.out.print(str);
        } else {
            this.buffer.append(str);
        }
    }

    /**
     * Cursors to a specific point in this instance, for chaining.
     *
     * @param  x the x
     * @param  y the y
     * 
     * @return   this instance, for chaining
     */
    public ConsoleWindow cursorTo(int x, int y) {
        this.writeCommand(AnsiCommand.CURSOR_MOVE, y + 1, x + 1);

        return this;
    }

    /**
     * Cursors up by a specified amount.
     *
     * @param  amount the amount
     * 
     * @return        this instance, for chaining
     */
    public ConsoleWindow cursorUp(int amount) {
        this.writeCommand(AnsiCommand.CURSOR_UP, amount);

        return this;
    }

    /**
     * Cursors down by a specified amount.
     *
     * @param  amount the amount
     * 
     * @return        this instance, for chaining
     */
    public ConsoleWindow cursorDown(int amount) {
        this.writeCommand(AnsiCommand.CURSOR_DOWN, amount);

        return this;
    }

    /**
     * Cursors left by a specified amount.
     *
     * @param  amount the amount
     * 
     * @return        this instance, for chaining
     */
    public ConsoleWindow cursorLeft(int amount) {
        this.writeCommand(AnsiCommand.CURSOR_LEFT, amount);

        return this;
    }

    /**
     * Cursors right by a specified amount.
     *
     * @param  amount the amount
     * 
     * @return        this instance, for chaining
     */
    public ConsoleWindow cursorRight(int amount) {
        this.writeCommand(AnsiCommand.CURSOR_RIGHT, amount);

        return this;
    }

    /**
     * Saves the cursor position.
     *
     * @return this instance, for chaining
     */
    public ConsoleWindow saveCursorPosition() {
        this.writeCommand(AnsiCommand.CURSOR_SAVE);

        return this;
    }

    /**
     * Restores the saved cursor position.
     *
     * @return this instance, for chaining
     */
    public ConsoleWindow restoreCursorPosition() {
        this.writeCommand(AnsiCommand.CURSOR_RESTORE);

        return this;
    }

    /**
     * Clears the display
     *
     * @return this instance, for chaining
     */
    public ConsoleWindow clearScreen() {
        this.writeCommand(AnsiCommand.ERASE_DISPLAY);

        return this;
    }

    /**
     * Writes text. This has a built in String.format implementation.
     *
     * @param  format the format
     * @param  args   the args
     * 
     * @return        this instance, for chaining
     */
    public ConsoleWindow write(@Nullable Object format, @Nullable Object... args) {
        String line = LoggingUtil.parseFormat(format, args);

        this.writeOut(line);

        return this;
    }

    /**
     * Writes text at a specified location. This has a built in String.format
     * implementation.
     *
     * @param  x      the x
     * @param  y      the y
     * @param  format the format
     * @param  args   the args
     * 
     * @return        this instance, for chaining
     */
    public ConsoleWindow writeAt(int x, int y, @Nullable Object format, @Nullable Object... args) {
        String line = LoggingUtil.parseFormat(format, args);

        this.saveCursorPosition();
        this.cursorTo(x, y);
        this.writeOut(line);
        this.restoreCursorPosition();

        return this;
    }

    /**
     * Prints a loading bar.
     *
     * @param  style       the style
     * @param  progress    the progress
     * @param  size        the size
     * @param  showPercent the show percent
     * 
     * @return             this instance, for chaining
     */
    public ConsoleWindow loadingBar(@NonNull BarStyle style, double progress, int size, boolean showPercent) {
        this.write(style.format(progress, size, showPercent));

        return this;
    }

    /**
     * Prints a loading bar.
     *
     * @param  x           the x
     * @param  y           the y
     * @param  style       the style
     * @param  progress    the progress
     * @param  size        the size
     * @param  showPercent the show percent
     * 
     * @return             this instance, for chaining
     */
    public ConsoleWindow loadingBar(int x, int y, @NonNull BarStyle style, double progress, int size, boolean showPercent) {
        this.saveCursorPosition();
        this.cursorTo(x, y);
        this.write(style.format(progress, size, showPercent));
        this.restoreCursorPosition();

        return this;
    }

    /**
     * Replaces a line with specified format. This has a built in String.format
     * implementation.
     *
     * @param  format the format
     * @param  args   the args
     * 
     * @return        this instance, for chaining
     */
    public ConsoleWindow replaceLine(@Nullable Object format, @Nullable Object... args) {
        String line = LoggingUtil.parseFormat(format, args);

        this.writeCommand(AnsiCommand.ERASE_LINE);
        this.writeOut(line);

        return this;
    }

    /**
     * Replaces a specified line with specified format. This has a built in
     * String.format implementation.
     *
     * @param  y      the y
     * @param  format the format
     * @param  args   the args
     * 
     * @return        this instance, for chaining
     */
    public ConsoleWindow replaceLineAt(int y, @Nullable Object format, @Nullable Object... args) {
        String line = LoggingUtil.parseFormat(format, args);

        this.saveCursorPosition();
        this.cursorTo(0, y);
        this.writeCommand(AnsiCommand.ERASE_LINE);
        this.writeOut(line);
        this.restoreCursorPosition();

        return this;
    }

    /**
     * Sets the text color.
     *
     * @param  color the color
     * 
     * @return       this instance, for chaining
     */
    public ConsoleWindow setTextColor(@NonNull ConsoleColor color) {
        this.writeOut(color.getForeground());

        return this;
    }

    /**
     * Sets the background color.
     *
     * @param  color the color
     * 
     * @return       this instance, for chaining
     */
    public ConsoleWindow setBackgroundColor(@NonNull ConsoleColor color) {
        this.writeOut(color.getBackground());

        return this;
    }

    /**
     * Clears the current line.
     *
     * @return this instance, for chaining
     */
    public ConsoleWindow clearLine() {
        this.writeCommand(AnsiCommand.ERASE_LINE);

        return this;
    }

    /**
     * Clears the line denoted by the y position parameter.
     *
     * @param  y the y
     * 
     * @return   this instance, for chaining
     */
    public ConsoleWindow clearLine(int y) {
        this.saveCursorPosition();
        this.cursorTo(0, y);
        this.clearLine();
        this.restoreCursorPosition();

        return this;
    }

    /**
     * Sets the attributes.
     *
     * @param  attributes the attributes
     * 
     * @return            this instance, for chaining
     */
    public ConsoleWindow setAttributes(@NonNull ConsoleAttribute... attributes) {
        for (ConsoleAttribute attribute : attributes) {
            this.writeOut(attribute.getAnsi());
        }

        return this;
    }

    /**
     * Updates the screen, flushing all content to the window.
     *
     * @return this instance, for chaining
     */
    public ConsoleWindow update() {
        System.out.print(this.buffer);
        System.out.flush();
        this.buffer.setLength(0);

        return this;
    }

    /**
     * Clears the buffer.
     *
     * @return this instance, for chaining
     */
    public ConsoleWindow clearBuffer() {
        this.buffer.setLength(0);

        return this;
    }

    /**
     * Gets the buffered string.
     *
     * @return the buffer
     */
    public String getBuffer() {
        return this.buffer.toString();
    }

    /**
     * To string.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return this.buffer.toString();
    }

}
