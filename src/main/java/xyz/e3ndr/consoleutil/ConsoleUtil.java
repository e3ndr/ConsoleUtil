package xyz.e3ndr.consoleutil;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.UUID;

import lombok.Getter;
import lombok.NonNull;
import xyz.e3ndr.consoleutil.consolewindow.ConsoleWindow;
import xyz.e3ndr.consoleutil.consolewindow.impl.AttachedConsoleWindow;
import xyz.e3ndr.consoleutil.consolewindow.impl.RemoteConsoleWindow;
import xyz.e3ndr.consoleutil.platform.PlatformHandler;
import xyz.e3ndr.consoleutil.platform.UnknownPlatformHandler;

public class ConsoleUtil {
    private static final @Getter @NonNull PlatformHandler handler = PlatformHandler.get();
    private static AttachedConsoleWindow window = new AttachedConsoleWindow();

    static {
        if (handler instanceof UnknownPlatformHandler) {
            System.err.println("Could not detect system type.");
            System.err.printf("Please report the system type \"%s\" to the developers (Make an issue report).\n", System.getProperty("os.name", "Unknown"));
        }

        try {
            handler.setup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Rings the bell.
     */
    public static void bell() {
        Toolkit.getDefaultToolkit().beep();
    }

    /**
     * Gets the currently attached console window.
     */
    public static ConsoleWindow getAttachedConsoleWindow() {
        return window;
    }

//    /**
//     * Sets whether or not characters should be inserted when typed.
//     *
//     * @throws IOException                   Signals that an I/O exception has
//     *                                       occurred during the underlying system
//     *                                       call.
//     * @throws InterruptedException          if there is an error while waiting for
//     *                                       a system call.
//     * @throws UnsupportedOperationException if there is no system specific
//     *                                       implementation.
//     */
//    public static void setEchoEnabled(boolean enabled) throws IOException, InterruptedException {
//        out.print("");
//        out.flush();
//    }

    /**
     * Opens another console window that you can interact with separately.
     *
     * @throws IOException                   Signals that an I/O exception has
     *                                       occurred during the underlying system
     *                                       call.
     * @throws InterruptedException          if there is an error while waiting for
     *                                       a system call.
     * @throws UnsupportedOperationException if there is no system specific
     *                                       implementation.
     */
    public static ConsoleWindow openAnotherConsoleWindow() throws IOException, InterruptedException {
        String ipcId = UUID.randomUUID().toString();

        return new RemoteConsoleWindow(ipcId);
    }

    /**
     * Gets the size of the console window.
     *
     * @return                               the dimensions of the console window.
     *
     * @throws IOException                   Signals that an I/O exception has
     *                                       occurred during the underlying system
     *                                       call.
     * @throws InterruptedException          if there is an error while waiting for
     *                                       a system call.
     * @throws UnsupportedOperationException if there is no system specific
     *                                       implementation.
     */
    public static Dimension getSize() throws IOException, InterruptedException {
        return handler.getSize();
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
    public static void setTitle(@NonNull String title) throws IOException, InterruptedException {
        handler.setTitle(title);
    }

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

        boolean isNotRunningInConsole = ((System.console() == null) && status.equalsIgnoreCase("false"));

        if (isNotRunningInConsole || status.equalsIgnoreCase("force")) {
            String jvmArgs = String.join(" ", ManagementFactory.getRuntimeMXBean().getInputArguments());
            String entry = System.getProperty("sun.java.command", ""); // Tested, present in OpenJDK and Oracle
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

            System.out.println("Program requested restart under a console window.");

            System.exit(0); // Orphan the child process
        } else {
            // Required on MacOS.
            // It won't *normally* cause issues on any other system, so it's cool.
            clearConsole();
        }
    }

    /**
     * Opens a console window, running the given command.
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

}
