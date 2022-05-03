package xyz.e3ndr.consoleutil.consolewindow;

import java.awt.Dimension;
import java.io.Closeable;
import java.io.IOException;

import org.jetbrains.annotations.Nullable;

import lombok.NonNull;
import xyz.e3ndr.consoleutil.ansi.ConsoleAttribute;
import xyz.e3ndr.consoleutil.ansi.ConsoleColor;

public interface ConsoleWindow extends Closeable {

    /**
     * Cursors to a specific point in this instance, for chaining.
     *
     * @param  x the x
     * @param  y the y
     * 
     * @return   this instance, for chaining
     */
    public ConsoleWindow cursorTo(int x, int y);

    /**
     * Cursors up by a specified amount.
     *
     * @param  amount the amount
     * 
     * @return        this instance, for chaining
     */
    public ConsoleWindow cursorUp(int amount);

    /**
     * Cursors down by a specified amount.
     *
     * @param  amount the amount
     * 
     * @return        this instance, for chaining
     */
    public ConsoleWindow cursorDown(int amount);

    /**
     * Cursors left by a specified amount.
     *
     * @param  amount the amount
     * 
     * @return        this instance, for chaining
     */
    public ConsoleWindow cursorLeft(int amount);

    /**
     * Cursors right by a specified amount.
     *
     * @param  amount the amount
     * 
     * @return        this instance, for chaining
     */
    public ConsoleWindow cursorRight(int amount);

    /**
     * Saves the cursor position.
     *
     * @return this instance, for chaining
     */
    public ConsoleWindow saveCursorPosition();

    /**
     * Restores the saved cursor position.
     *
     * @return this instance, for chaining
     */
    public ConsoleWindow restoreCursorPosition();

    /**
     * Clears the display
     *
     * @return this instance, for chaining
     */
    public ConsoleWindow clearScreen();

    /**
     * Writes text. This has a built in String.format implementation.
     *
     * @param  format the format
     * @param  args   the args
     * 
     * @return        this instance, for chaining
     */
    public ConsoleWindow write(@Nullable Object format, @Nullable Object... args);

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
    public ConsoleWindow writeAt(int x, int y, @Nullable Object format, @Nullable Object... args);

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
    public ConsoleWindow loadingBar(@NonNull BarStyle style, double progress, int size, boolean showPercent);

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
    public ConsoleWindow loadingBarAt(int x, int y, @NonNull BarStyle style, double progress, int size, boolean showPercent);

    /**
     * Replaces a line with specified format. This has a built in String.format
     * implementation.
     *
     * @param  format the format
     * @param  args   the args
     * 
     * @return        this instance, for chaining
     */
    public ConsoleWindow replaceLine(@Nullable Object format, @Nullable Object... args);

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
    public ConsoleWindow replaceLineAt(int y, @Nullable Object format, @Nullable Object... args);

    /**
     * Sets the text color.
     *
     * @param  color the color
     * 
     * @return       this instance, for chaining
     */
    public ConsoleWindow setTextColor(@NonNull ConsoleColor color);

    /**
     * Sets the background color.
     *
     * @param  color the color
     * 
     * @return       this instance, for chaining
     */
    public ConsoleWindow setBackgroundColor(@NonNull ConsoleColor color);

    /**
     * Clears the current line.
     *
     * @return this instance, for chaining
     */
    public ConsoleWindow clearLine();

    /**
     * Clears the line denoted by the y position parameter.
     *
     * @param  y the y
     * 
     * @return   this instance, for chaining
     */
    public ConsoleWindow clearLine(int y);

    /**
     * Sets the attributes.
     *
     * @param  attributes the attributes
     * 
     * @return            this instance, for chaining
     */
    public ConsoleWindow setAttributes(@NonNull ConsoleAttribute... attributes);

    /**
     * Updates the screen, flushing all content to the window.
     *
     * @return this instance, for chaining
     */
    public ConsoleWindow flush();

    /**
     * Clears the buffer.
     *
     * @return this instance, for chaining
     */
    public ConsoleWindow clearBuffer();

    /**
     * Sets whether or not the write methods are automatically flushing to console.
     * 
     * @return this instance, for chaining
     */
    public ConsoleWindow setAutoFlushing(boolean autoFlushing);

    /**
     * @return whether or not the write methods are automatically flushing to
     *         console.
     * 
     * @see    #setAutoFlushing(boolean)
     */
    public boolean isAutoFlushing();

    /**
     * Rings the bell.
     * 
     * @return this instance, for chaining
     */
    public ConsoleWindow bell();

    /**
     * Sets the width and height of the console.
     * 
     * @return                      this instance, for chaining
     * 
     * @throws InterruptedException
     * @throws IOException
     */
    public ConsoleWindow setSize(int width, int height) throws IOException, InterruptedException;

    /**
     * Gets the width and height of the console.
     * 
     * @return                      the dimensions
     * 
     * @throws InterruptedException
     * @throws IOException
     */
    public Dimension getSize() throws IOException, InterruptedException;

    /**
     * Sets the title of the console.
     * 
     * @return                      this instance, for chaining
     * 
     * @throws InterruptedException
     * @throws IOException
     */
    public ConsoleWindow setTitle(String title) throws IOException, InterruptedException;

    /**
     * Resets the current color.
     *
     * @param  foreground           whether or not to reset the foreground
     * @param  background           whether or not to reset the background
     * 
     * @throws IOException          Signals that an I/O exception has occurred
     *                              during the underlying system call.
     * @throws InterruptedException if there is an error while waiting for a system
     *                              call.
     */
    default void resetColor(boolean foreground, boolean background) throws IOException, InterruptedException {
        if (foreground) {
            write("\u001b[39m");
        }

        if (background) {
            write("\u001b[49m");
        }

        update();
    }

    public static String sanitize(String string) {
        return string.replace("\u001b", "^");
    }

}
