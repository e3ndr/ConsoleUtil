package xyz.e3ndr.consoleutil.platform;

import java.awt.Dimension;
import java.io.IOException;

import lombok.NonNull;

public interface PlatformHandler {

    public void clearConsole() throws IOException, InterruptedException;

    public void setTitle(@NonNull String title) throws IOException, InterruptedException;

    public void setSize(int width, int height) throws IOException, InterruptedException;

    public Dimension getSize() throws IOException, InterruptedException;

    public void startConsoleWindow(String cmdLine) throws IOException, InterruptedException;

}
