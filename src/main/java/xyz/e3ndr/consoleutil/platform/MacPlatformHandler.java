package xyz.e3ndr.consoleutil.platform;

import java.io.IOException;

public class MacPlatformHandler extends UnixPlatformHandler {

    @Override
    public void startConsoleWindow(String cmdLine) throws IOException, InterruptedException {
        cmdLine = String.format(
            "tell application \"Terminal\" to do script \"%s\"",
            cmdLine
                .replace("\"", "\\\"")
                .replace("'", "\\'")
        );

        new ProcessBuilder("osascript", "-e", cmdLine).start();
    }

}
