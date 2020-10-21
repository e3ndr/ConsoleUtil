package xyz.e3ndr.consoleutil.platform.platforms;

import java.io.IOException;

import lombok.NonNull;
import xyz.e3ndr.consoleutil.platform.PlatformHandler;
import xyz.e3ndr.fastloggingframework.logging.FastLogger;

public class UnixPlatformHandler implements PlatformHandler {
    @SuppressWarnings("unused")
    private FastLogger logger = new FastLogger("ConsoleUtil.UnixPlatformHandler");

    @Override
    public void clearConsole() throws IOException, InterruptedException {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    @Override
    public void setTitle(@NonNull String title) throws IOException, InterruptedException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSize(int width, int height) throws IOException, InterruptedException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void summonConsoleWindow() throws IOException, InterruptedException {
        throw new UnsupportedOperationException();
    }

}
