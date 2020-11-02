package xyz.e3ndr.consoleutil.platform.platforms;

import java.io.IOException;

import lombok.NonNull;
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
        this.run("title " + title).inheritIO().start();
    }

    @Override
    public void setSize(int width, int height) throws IOException, InterruptedException {
        this.run("mode con: cols=" + width + " lines=" + height).inheritIO().start().waitFor();
    }

    @Override
    public void summonConsoleWindow(String line) throws IOException, InterruptedException {
        this.logger.info("Program requested restart under a console window.");

        this.run(String.format("start \"\" %s", line)).start();

        FastLoggingFramework.close(); // Flush logger
        System.exit(0); // Orphan the child process
    }

    private ProcessBuilder run(String command) {
        this.logger.debug("Running command: %s", command);
        return new ProcessBuilder("cmd", "/c", command);
    }

}
