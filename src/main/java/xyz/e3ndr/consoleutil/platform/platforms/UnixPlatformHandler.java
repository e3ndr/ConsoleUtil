package xyz.e3ndr.consoleutil.platform.platforms;

import java.io.IOException;

import lombok.NonNull;
import xyz.e3ndr.consoleutil.platform.PlatformHandler;

public class UnixPlatformHandler implements PlatformHandler {

    @Override
    public void clearConsole() throws IOException {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    @Override
    public void setTitle(@NonNull String title) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSize(int width, int height) throws IOException {
        throw new UnsupportedOperationException();
    }

}
