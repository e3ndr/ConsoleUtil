package xyz.e3ndr.consoleutil;

import java.io.IOException;

import lombok.Getter;
import lombok.NonNull;
import xyz.e3ndr.consoleutil.platform.JavaPlatform;
import xyz.e3ndr.consoleutil.platform.PlatformHandler;
import xyz.e3ndr.fastloggingframework.logging.FastLogger;

public class ConsoleUtil {
    private static @Getter final JavaPlatform platform = JavaPlatform.get();
    private static @Getter @NonNull PlatformHandler handler;
    private static FastLogger logger = new FastLogger();

    static {
        if (platform == JavaPlatform.UNKNOWN) {
            logger.warn("Could not detect system type, defaulting to a console size of 40,10");
            logger.warn("Please report the system type \"%s\" to the developers (Make an issue report).", System.getProperty("os.name"));
        }

        handler = platform.getHandler(); // Allows programs to manually set a handler.

        logger.debug("Running under %s on %s", System.getProperty("java.vm.name", "Unknown Runtime"), System.getProperty("os.name", "Unknown Operating System"));
    }

    /**
     * Clears the console window.
     *
     * @throws IOException Signals that an I/O exception has occurred during the underlying system call.
     * @throws InterruptedException if there is an error while waiting for a system call.
     * @throws UnsupportedOperationException if there is no system specific implementation.
     */
    public static void clearConsole() throws IOException, InterruptedException {
        handler.clearConsole();
    }

    /**
     * Sets the title.
     *
     * @param title the new title
     * @throws IOException Signals that an I/O exception has occurred during the underlying system call.
     * @throws InterruptedException if there is an error while waiting for a system call.
     * @throws UnsupportedOperationException if there is no system specific implementation.
     */
    public static void setTitle(@NonNull String title) throws IOException, InterruptedException {
        handler.setTitle(title);
    }

    /**
     * Sets the size.
     *
     * @param width the width
     * @param height the height
     * @throws IOException Signals that an I/O exception has occurred during the underlying system call.
     * @throws InterruptedException if there is an error while waiting for a system call.
     * @throws UnsupportedOperationException if there is no system specific implementation.
     */
    public static void setSize(int width, int height) throws IOException, InterruptedException {
        handler.setSize(width, height);
    }

    /**
     * Restarts the JVM with a console window.
     *
     * @throws IOException Signals that an I/O exception has occurred during the underlying system call.
     * @throws InterruptedException if there is an error while waiting for a system call.
     * @throws UnsupportedOperationException if there is no system specific implementation.
     */
    public void summonConsoleWindow() throws IOException, InterruptedException {
        handler.summonConsoleWindow();
    }

    /**
     * Sets the platform handler.
     *
     * @param newHandler the new handler
     */
    @Deprecated
    public static void setHandler(@NonNull PlatformHandler newHandler) {
        handler = newHandler;
    }

}
