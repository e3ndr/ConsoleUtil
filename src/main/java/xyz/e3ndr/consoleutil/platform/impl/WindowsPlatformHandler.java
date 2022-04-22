package xyz.e3ndr.consoleutil.platform.impl;

import java.awt.Dimension;
import java.io.IOException;

import io.github.alexarchambault.windowsansi.WindowsAnsi;
import io.github.alexarchambault.windowsansi.WindowsAnsi.Size;
import lombok.NonNull;
import xyz.e3ndr.consoleutil.platform.PlatformHandler;

public class WindowsPlatformHandler implements PlatformHandler {

    @Override
    public void clearConsole() throws IOException, InterruptedException {
        this.run("cls").inheritIO().start().waitFor();
    }

    @Override
    public void setTitle(@NonNull String title) throws IOException, InterruptedException {
        this.run("title " + title.replace("^", "^^").replace("|", "^|")).inheritIO().start();
    }

    @Override
    public void setSize(int width, int height) throws IOException, InterruptedException {
        this.run("mode con: cols=" + width + " lines=" + height).inheritIO().start().waitFor();
    }

    @Override
    public void startConsoleWindow(String cmdLine) throws IOException, InterruptedException {
        this.run(String.format("start \"\" %s", cmdLine)).start();
    }

    private ProcessBuilder run(String command) {
        return new ProcessBuilder("cmd", "/c", command);
    }

    @Override
    public Dimension getSize() throws IOException, InterruptedException {
        Size s = WindowsAnsi.terminalSize();

        return new Dimension(
            s.getWidth(),
            s.getHeight()
        );
    }

}
