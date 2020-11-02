package xyz.e3ndr.consoleutil.platform.platforms;

import java.io.IOException;

import lombok.NonNull;
import xyz.e3ndr.consoleutil.platform.PlatformHandler;
import xyz.e3ndr.fastloggingframework.FastLoggingFramework;
import xyz.e3ndr.fastloggingframework.logging.FastLogger;

public class UnixPlatformHandler implements PlatformHandler {
    private FastLogger logger = new FastLogger("ConsoleUtil.UnixPlatformHandler");

    @Override
    public void clearConsole() throws IOException, InterruptedException {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    @Override
    public void setTitle(@NonNull String title) throws IOException, InterruptedException {
        System.out.printf("\033]0;%s\007", title);
        System.out.flush();
    }

    @Override
    public void setSize(int width, int height) throws IOException, InterruptedException {
        System.out.printf("\033[8;%d;%d6", width, height);
        System.out.flush();
    }

    @Override
    public void summonConsoleWindow(String line) throws IOException, InterruptedException {
        this.logger.info("Program requested restart under a console window.");

        new ProcessBuilder("x-terminal-emulator", "-e", '"' + line + '"').start();

        FastLoggingFramework.close(); // Flush logger
        System.exit(0); // Orphan the child process
    }

}
