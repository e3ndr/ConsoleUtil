package xyz.e3ndr.consoleutil.platform.platforms;

import java.io.IOException;

import lombok.NonNull;
import xyz.e3ndr.consoleutil.platform.PlatformHandler;

public class UnknownPlatformHandler implements PlatformHandler {

    @Override
    public void clearConsole() throws InterruptedException, IOException {
        // Not Supported
    }

    @Override
    public void setTitle(@NonNull String title) throws InterruptedException, IOException {
        // Not Supported
    }

    @Override
    public void setSize(int width, int height) throws InterruptedException, IOException {
        // Not Supported
    }

}
