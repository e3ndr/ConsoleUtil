package xyz.e3ndr.consoleutil.platform;

import java.awt.Dimension;
import java.io.IOException;

import lombok.NonNull;

public class UnknownPlatformHandler implements PlatformHandler {

    @Override
    public void clearConsole() throws IOException, InterruptedException {
        throw new UnsupportedOperationException();
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
    public void startConsoleWindow(String cmdLine) throws IOException, InterruptedException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Dimension getSize() throws IOException, InterruptedException {
        throw new UnsupportedOperationException();
    }

}
