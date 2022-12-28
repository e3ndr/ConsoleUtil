package xyz.e3ndr.consoleutil.platform;

import java.awt.Dimension;
import java.io.IOException;

import co.casterlabs.commons.platform.OSDistribution;
import co.casterlabs.commons.platform.Platform;
import lombok.NonNull;

public interface PlatformHandler {

    public static PlatformHandler get() {
        switch (Platform.osFamily) {
            case WINDOWS:
                return new WindowsPlatformHandler();

            case UNIX:
                if (Platform.osDistribution == OSDistribution.MACOS) {
                    return new MacPlatformHandler();
                } else {
                    return new UnixPlatformHandler();
                }

            default:
                return new UnknownPlatformHandler();
        }
    }

    default void setup() throws IOException, InterruptedException {}

    public void clearConsole() throws IOException, InterruptedException;

    public void setTitle(@NonNull String title) throws IOException, InterruptedException;

    public void setSize(int width, int height) throws IOException, InterruptedException;

    public Dimension getSize() throws IOException, InterruptedException;

    public void startConsoleWindow(String cmdLine) throws IOException, InterruptedException;

}
