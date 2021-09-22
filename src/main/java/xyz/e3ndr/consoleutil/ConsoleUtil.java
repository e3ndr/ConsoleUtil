package xyz.e3ndr.consoleutil;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;

import jline.Terminal;
import jline.TerminalFactory;
import lombok.Getter;
import lombok.NonNull;
import xyz.e3ndr.consoleutil.platform.JavaPlatform;
import xyz.e3ndr.consoleutil.platform.PlatformHandler;
import xyz.e3ndr.fastloggingframework.FastLoggingFramework;
import xyz.e3ndr.fastloggingframework.logging.FastLogger;

public class ConsoleUtil {
    private static @Getter final JavaPlatform platform = JavaPlatform.get();
    private static @Getter Terminal jLine = TerminalFactory.get();
    private static @Getter @NonNull PlatformHandler handler;
    private static FastLogger logger = new FastLogger();

    static {
        if (platform == JavaPlatform.UNKNOWN) {
            logger.warn("Could not detect system type, defaulting to a console size of 40,10");
            logger.warn("Please report the system type \"%s\" to the developers (Make an issue report).", System.getProperty("os.name"));
        }

        handler = platform.getHandler(); // Allows programs to manually set a handler.

        try {
            jLine.init();
        } catch (Exception e) {
            logger.severe("Unable to initialize JLine: %s", e);
        }

        logger.debug("Running under %s on %s", System.getProperty("java.vm.name", "Unknown Runtime"), System.getProperty("os.name", "Unknown Operating System"));
    }

    /**
     * Rings the bell.
     */
    public static void bell() {
        Toolkit.getDefaultToolkit().beep();
    }

    /**
     * Gets the size of the console window.
     *
     * @return the dimensions
     */
    public static Dimension getSize() {
        return new Dimension(jLine.getWidth(), jLine.getHeight());
    }

    /**
     * Clears the console window.
     *
     * @throws IOException                   Signals that an I/O exception has
     *                                       occurred during the underlying system
     *                                       call.
     * @throws InterruptedException          if there is an error while waiting for
     *                                       a system call.
     * @throws UnsupportedOperationException if there is no system specific
     *                                       implementation.
     */
    public static void clearConsole() throws IOException, InterruptedException {
        handler.clearConsole();
    }

    /**
     * Sets the title.
     *
     * @param title the new title
     * 
     * @throws IOException                   Signals that an I/O exception has
     *                                       occurred during the underlying system
     *                                       call.
     * @throws InterruptedException          if there is an error while waiting for
     *                                       a system call.
     * @throws UnsupportedOperationException if there is no system specific
     *                                       implementation.
     */
    public static void setTitle(@NonNull String title) throws IOException, InterruptedException {
        handler.setTitle(title);
    }

    /**
     * Sets the size.
     *
     * @param width  the width
     * @param height the height
     * 
     * @throws IOException                   Signals that an I/O exception has
     *                                       occurred during the underlying system
     *                                       call.
     * @throws InterruptedException          if there is an error while waiting for
     *                                       a system call.
     * @throws UnsupportedOperationException if there is no system specific
     *                                       implementation.
     */
    public static void setSize(int width, int height) throws IOException, InterruptedException {
        handler.setSize(width, height);
    }

    /**
     * Restarts the JVM with a console window.
     *
     * @throws IOException                   Signals that an I/O exception has
     *                                       occurred during the underlying system
     *                                       call.
     * @throws InterruptedException          if there is an error while waiting for
     *                                       a system call.
     * @throws UnsupportedOperationException if there is no system specific
     *                                       implementation.
     */
    public static void summonConsoleWindow() throws IOException, InterruptedException {
        String status = System.getProperty("StartedWithConsole", "false");

        if (((System.console() == null) && status.equalsIgnoreCase("false")) || status.equalsIgnoreCase("force")) {
            String jvmArgs = String.join(" ", ManagementFactory.getRuntimeMXBean().getInputArguments());
            String entry = System.getProperty("sun.java.command"); // Tested, present in OpenJDK and Oracle
            String classpath = System.getProperty("java.class.path");
            String javaHome = System.getProperty("java.home");

            String[] args = entry.split(" ");
            File entryFile = new File(args[0]);

            if (entryFile.exists()) { // If the entry is a file, not a main method.
                args[0] = '"' + entryFile.getCanonicalPath() + '"'; // Use raw file path.

                handler.startConsoleWindow(String.format("\"%s/bin/java\" -DStartedWithConsole=true %s -cp \"%s\" -jar %s", javaHome, jvmArgs, classpath, String.join(" ", args)));
            } else {
                handler.startConsoleWindow(String.format("\"%s/bin/java\" -DStartedWithConsole=true %s -cp \"%s\" %s", javaHome, jvmArgs, classpath, entry));
            }

            FastLogger.logStatic("Program requested restart under a console window.");

            FastLoggingFramework.close(); // Flush logger
            System.exit(0); // Orphan the child process
        }
    }

    /**
     * Restarts the JVM with a console window.
     *
     * @throws IOException                   Signals that an I/O exception has
     *                                       occurred during the underlying system
     *                                       call.
     * @throws InterruptedException          if there is an error while waiting for
     *                                       a system call.
     * @throws UnsupportedOperationException if there is no system specific
     *                                       implementation.
     */
    public static void startConsoleWindow(String cmdLine) throws IOException, InterruptedException {
        handler.startConsoleWindow(cmdLine);
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
