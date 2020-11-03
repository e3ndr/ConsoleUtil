package xyz.e3ndr.consoleutil.platform;

import java.io.IOException;

import lombok.NonNull;

/**
 * The Interface PlatformHandler. This allows abstraction between platforms.
 */
public interface PlatformHandler {

    /**
     * Clear console.
     *
     * @throws IOException                   Signals that an I/O exception has
     *                                       occurred during the underlying system
     *                                       call.
     * @throws InterruptedException          if there is an error while waiting for
     *                                       a system call.
     * @throws UnsupportedOperationException if there is no system specific
     *                                       implementation.
     */
    public void clearConsole() throws IOException, InterruptedException;

    /**
     * Sets the title.
     *
     * @param  title                         the new title
     * 
     * @throws IOException                   Signals that an I/O exception has
     *                                       occurred during the underlying system
     *                                       call.
     * @throws InterruptedException          if there is an error while waiting for
     *                                       a system call.
     * @throws UnsupportedOperationException if there is no system specific
     *                                       implementation.
     */
    public void setTitle(@NonNull String title) throws IOException, InterruptedException;

    /**
     * Sets the size.
     *
     * @param  width                         the width
     * @param  height                        the height
     * 
     * @throws IOException                   Signals that an I/O exception has
     *                                       occurred during the underlying system
     *                                       call.
     * @throws InterruptedException          if there is an error while waiting for
     *                                       a system call.
     * @throws UnsupportedOperationException if there is no system specific
     *                                       implementation.
     */
    public void setSize(int width, int height) throws IOException, InterruptedException;

    /**
     * Restarts the JVM with a console window.
     * 
     * @param  line
     *
     * @throws IOException                   Signals that an I/O exception has
     *                                       occurred during the underlying system
     *                                       call.
     * @throws InterruptedException          if there is an error while waiting for
     *                                       a system call.
     * @throws UnsupportedOperationException if there is no system specific
     *                                       implementation.
     */
    public void summonConsoleWindow(String line) throws IOException, InterruptedException;

}
