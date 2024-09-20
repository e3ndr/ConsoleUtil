package xyz.e3ndr.consoleutil.platform;

import java.awt.Dimension;
import java.io.IOException;
import java.nio.charset.Charset;

import co.casterlabs.commons.io.streams.StreamUtil;
import lombok.NonNull;

public class UnixPlatformHandler implements PlatformHandler {

    @Override
    public void clearConsole() throws IOException, InterruptedException {
        System.out.print("\u001b[H\u001b[2J");
        System.out.flush();
    }

    @Override
    public void setTitle(@NonNull String title) throws IOException, InterruptedException {
        System.out.printf("\u001b]0;%s\007", title);
        System.out.flush();
    }

    @Override
    public void setSize(int width, int height) throws IOException, InterruptedException {
        System.out.printf("\u001b[8;%d;%d6", width, height);
        System.out.flush();
    }

    @Override
    public void startConsoleWindow(String cmdLine) throws IOException, InterruptedException {
        // TODO Doesn't exactly work, as it usually cannot access the directory.
        // (Unless you give it sudo)
        if (isGnome()) {
            // GNOME is special, so we have to specify that we want a new window.
            new ProcessBuilder("gnome-terminal", "--window", "--", cmdLine).start();
        } else {
            // Try a generic command
            new ProcessBuilder("x-terminal-emulator", "-e", cmdLine).start();
        }
    }

    public static boolean isGnome() {
        return System.getenv("GNOME_DESKTOP_SESSION_ID") != null;
    }

    @Override
    public Dimension getSize() throws IOException, InterruptedException {
        String[] sizeResult = StreamUtil.toString(
            new ProcessBuilder("stty", "size")
                .start()
                .getInputStream(),
            Charset.defaultCharset()
        )
            .trim()
            .split(" ");

        int height = Integer.parseInt(sizeResult[0]);
        int width = Integer.parseInt(sizeResult[1]);

        return new Dimension(width, height);
    }

}
