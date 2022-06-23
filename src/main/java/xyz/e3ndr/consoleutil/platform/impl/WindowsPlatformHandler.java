package xyz.e3ndr.consoleutil.platform.impl;

import java.awt.Dimension;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import co.casterlabs.rakurai.io.IOUtil;
import lombok.NonNull;
import xyz.e3ndr.consoleutil.platform.PlatformHandler;

public class WindowsPlatformHandler implements PlatformHandler {
    private static String sizeScriptEncoded;

    static {
        try {
            String script = IOUtil.readString(WindowsPlatformHandler.class.getResourceAsStream("/windows_size.ps1"));
            script = String.format("& {\n%s\n}", script);

            sizeScriptEncoded = Base64.getEncoder()
                .encodeToString(script.getBytes(StandardCharsets.UTF_16LE));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
        Process process = new ProcessBuilder()
            .command(
                "powershell.exe",
                "-NoProfile",
                "-NonInteractive",
                "-EncodedCommand", sizeScriptEncoded
            )
            .inheritIO()
            .redirectError(Redirect.PIPE)
            .redirectOutput(Redirect.PIPE)
            .start();

        process.waitFor();

        String result = IOUtil.readString(process.getInputStream()).trim();
        String[] sizes = result.split(",");

        int width = 0;
        int height = 0;

        if (sizes.length == 2) {
            width = Integer.parseInt(sizes[0]);
            height = Integer.parseInt(sizes[1]);
        }

        return new Dimension(width, height);
    }

    public static void setup() throws IOException, InterruptedException {
        String script = IOUtil.readString(WindowsPlatformHandler.class.getResourceAsStream("/windows_setup.ps1"));
        script = String.format("& {\n%s\n}", script);

        String encodedScript = Base64.getEncoder()
            .encodeToString(script.getBytes(StandardCharsets.UTF_16LE));

        ProcessBuilder builder = new ProcessBuilder()
            .command(
                "powershell.exe",
                "-NoProfile",
                "-NonInteractive",
                "-EncodedCommand", encodedScript
            )
            .inheritIO()
            .redirectError(Redirect.PIPE);

        /*int ret = */builder.start().waitFor();
//        if (ret != 0) {
//            throw new IOException(String.format("Error running windows setup script (code: %d)", ret));
//        }
    }

}
