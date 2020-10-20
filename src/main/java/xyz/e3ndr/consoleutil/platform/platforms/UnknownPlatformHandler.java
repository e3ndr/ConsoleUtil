package xyz.e3ndr.consoleutil.platform.platforms;

import java.io.IOException;

import lombok.NonNull;
import xyz.e3ndr.consoleutil.platform.PlatformHandler;

public class UnknownPlatformHandler implements PlatformHandler {

    @Override
    public void clearConsole() throws IOException {
        throw new UnsupportedOperationException();
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
