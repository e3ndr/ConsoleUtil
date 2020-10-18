package xyz.e3ndr.consoleutil.platform;

import java.io.IOException;

import lombok.NonNull;

public interface PlatformHandler {

    public void clearConsole() throws InterruptedException, IOException;

    public void setTitle(@NonNull String title) throws InterruptedException, IOException;

    public void setSize(int width, int height) throws InterruptedException, IOException;

}
