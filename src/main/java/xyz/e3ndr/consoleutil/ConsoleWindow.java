package xyz.e3ndr.consoleutil;

import java.io.IOException;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.Ansi.Erase;
import org.jetbrains.annotations.Nullable;

import lombok.NonNull;
import xyz.e3ndr.fastloggingframework.logging.LoggingUtil;

public class ConsoleWindow {
    private Ansi ansi = Ansi.ansi();

    public ConsoleWindow() throws IOException, InterruptedException {
        ConsoleUtil.clearConsole();

        ConsoleUtil.getJLine().setEchoEnabled(false);
    }

    /**
     * Cursors to a specific point in the console window.
     *
     * @param  x the x position
     * @param  y the y position
     * 
     * @return   this instance, for chaining
     */
    public ConsoleWindow cursorTo(int x, int y) {
        this.ansi.cursor(y + 1, x + 1);

        return this;
    }

    /**
     * Saves the cursor position.
     *
     * @return this instance, for chaining
     */
    public ConsoleWindow saveCursor() {
        this.ansi.saveCursorPosition();

        return this;
    }

    /**
     * Restores the saved cursor position.
     *
     * @return this instance, for chaining
     */
    public ConsoleWindow restoreCursor() {
        this.ansi.restoreCursorPosition();

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

        this.ansi.a(line);

        return this;
    }

    /**
     * Writes text at a specified location. This has a built in String.format
     * implementation.
     *
     * @param  x      the x position
     * @param  y      the y position
     * @param  format the format
     * @param  args   the args
     * 
     * @return        this instance, for chaining
     */
    public ConsoleWindow writeAt(int x, int y, @Nullable Object format, @Nullable Object... args) {
        String line = LoggingUtil.parseFormat(format, args);

        this.ansi.saveCursorPosition();
        this.cursorTo(x, y);
        this.ansi.a(line);
        this.ansi.restoreCursorPosition();

        return this;
    }

    /**
     * Prints a loading bar.
     *
     * @param  style       the style to use, see {@link BarStyle}
     * @param  progress    the progress, between 0-1
     * @param  size        the size of the bar
     * @param  showPercent whether or not to show a percentage
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
     * @param  x           the x position
     * @param  y           the y position
     * @param  style       the style to use, see {@link BarStyle}
     * @param  progress    the progress, between 0-1
     * @param  size        the size of the bar
     * @param  showPercent whether or not to show a percentage
     * 
     * @return             this instance, for chaining
     */
    public ConsoleWindow loadingBar(int x, int y, @NonNull BarStyle style, double progress, int size, boolean showPercent) {
        this.ansi.saveCursorPosition();
        this.cursorTo(x, y);
        this.write(style.format(progress, size, showPercent));
        this.ansi.restoreCursorPosition();

        return this;
    }

    /**
     * Replaces a line with specified format.
     *
     * @param  format the format
     * @param  args   the args
     * 
     * @return        this instance, for chaining
     */
    public ConsoleWindow replaceLine(@Nullable Object format, @Nullable Object... args) {
        String line = LoggingUtil.parseFormat(format, args);

        this.ansi.eraseLine(Erase.ALL);
        this.ansi.a(line);

        return this;
    }

    /**
     * Replaces a specified line with specified format.
     *
     * @param  y      the y position
     * @param  format the format
     * @param  args   the args
     * 
     * @return        this instance, for chaining
     */
    public ConsoleWindow replaceLineAy(int y, @Nullable Object format, @Nullable Object... args) {
        String line = LoggingUtil.parseFormat(format, args);

        this.ansi.saveCursorPosition();
        this.cursorTo(0, y);
        this.ansi.eraseLine(Erase.ALL);
        this.ansi.a(line);
        this.ansi.restoreCursorPosition();

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
        if (color.isLight()) {
            this.ansi.fgBright(color.getAnsiColor());
        } else {
            this.ansi.fg(color.getAnsiColor());
        }

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
        if (color.isLight()) {
            this.ansi.bgBright(color.getAnsiColor());
        } else {
            this.ansi.bg(color.getAnsiColor());
        }

        return this;
    }

    /**
     * Clears the current line.
     *
     * @return this instance, for chaining
     */
    public ConsoleWindow clearLine() {
        this.ansi.eraseLine(Erase.ALL);

        return this;
    }

    /**
     * Clears the line denoted by the y position parameter.
     *
     * @param  y the y position
     * 
     * @return   this instance, for chaining
     */
    public ConsoleWindow clearLine(int y) {
        this.ansi.saveCursorPosition();
        this.cursorTo(0, y);
        this.ansi.eraseLine(Erase.ALL);
        this.ansi.restoreCursorPosition();

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
            for (Ansi.Attribute ansiAttr : attribute.getAnsiAttributes()) {
                this.ansi.a(ansiAttr);
            }
        }

        return this;
    }

    /**
     * Updates the screen, flushing all content to the window.
     *
     * @return this instance, for chaining
     */
    public ConsoleWindow update() {
        System.out.print(this.ansi);
        System.out.flush();
        this.ansi = Ansi.ansi();

        return this;
    }

}
