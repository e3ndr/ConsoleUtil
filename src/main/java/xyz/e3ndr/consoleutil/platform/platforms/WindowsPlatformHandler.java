package xyz.e3ndr.consoleutil.platform.platforms;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.URISyntaxException;

import lombok.NonNull;
import xyz.e3ndr.consoleutil.ConsoleUtil;
import xyz.e3ndr.consoleutil.platform.PlatformHandler;
import xyz.e3ndr.fastloggingframework.FastLoggingFramework;
import xyz.e3ndr.fastloggingframework.logging.FastLogger;

public class WindowsPlatformHandler implements PlatformHandler {
    private FastLogger logger = new FastLogger("ConsoleUtil.WindowsPlatformHandler");

    @Override
    public void clearConsole() throws IOException, InterruptedException {
        this.run("cls").inheritIO().start().waitFor();
    }

    @Override
    public void setTitle(@NonNull String title) throws IOException, InterruptedException {
        this.run("title " + title).inheritIO().start().waitFor();
    }

    @Override
    public void setSize(int width, int height) throws IOException, InterruptedException {
        this.run("mode con: cols=" + width + " lines=" + height).inheritIO().start();
    }

    @Override
    public void summonConsoleWindow() throws IOException, InterruptedException {
        try {
            if ((System.console() == null) && System.getProperty("StartedWithConsole", "false").equalsIgnoreCase("false")) {
                File codeSource = new File(ConsoleUtil.class.getProtectionDomain().getCodeSource().getLocation().toURI());
                String jvmArgs = String.join(" ", ManagementFactory.getRuntimeMXBean().getInputArguments());
                String entry = System.getProperty("sun.java.command"); // Tested, present in OpenJDK and Oracle
                String classpath = System.getProperty("java.class.path");
                String javaHome = System.getProperty("java.home");
                ProcessBuilder process;

                if (codeSource.isFile()) {
                    process = this.run(String.format("\"%s\\bin\\java\" -DStartedWithConsole=true %s -cp %s -jar %s", javaHome, jvmArgs, classpath, entry));
                } else {
                    process = this.run(String.format("\"%s\\bin\\java\" -DStartedWithConsole=true %s -cp %s %s", javaHome, jvmArgs, classpath, entry));
                }

                this.logger.info("Program requested restart under a console window.");

                process.inheritIO().start().waitFor();

                FastLoggingFramework.close(); // Flush logger
                System.exit(0); // Orphan the child process
            }
        } catch (URISyntaxException e) {
            throw new IOException(e);
        }
    }

    private ProcessBuilder run(String command) {
        this.logger.debug("Running command: %s", command);
        return new ProcessBuilder("cmd", "/c", command);
    }

}
