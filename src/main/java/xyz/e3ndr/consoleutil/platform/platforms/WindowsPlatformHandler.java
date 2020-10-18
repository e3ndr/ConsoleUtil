package xyz.e3ndr.consoleutil.platform.platforms;

import java.io.IOException;

import lombok.NonNull;
import xyz.e3ndr.consoleutil.platform.PlatformHandler;

public class WindowsPlatformHandler implements PlatformHandler {

    @Override
    public void clearConsole() throws InterruptedException, IOException {
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
    }

    @Override
    public void setTitle(@NonNull String title) throws InterruptedException, IOException {
        new ProcessBuilder("cmd", "/c", "title " + title).inheritIO().start();
    }

    @Override
    public void setSize(int width, int height) throws InterruptedException, IOException {
        new ProcessBuilder("cmd", "/c", "mode", "con:", "cols=" + width, "lines=" + height).inheritIO().start();
    }

}
