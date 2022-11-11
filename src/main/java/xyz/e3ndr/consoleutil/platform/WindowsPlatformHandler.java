package xyz.e3ndr.consoleutil.platform;

import java.awt.Dimension;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import co.casterlabs.rakurai.io.IOUtil;
import lombok.NonNull;

public class WindowsPlatformHandler implements PlatformHandler {
    private static String setupScriptEncoded;
    private static String sizeScriptEncoded;

    static {
        try {
            String script = IOUtil.readString(WindowsPlatformHandler.class.getResourceAsStream("/consoleutil/windows/setup.ps1"));
            script = String.format("& {\n%s\n}", script);

            setupScriptEncoded = encodeScript(script);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String script = IOUtil.readString(WindowsPlatformHandler.class.getResourceAsStream("/consoleutil/windows/size.ps1"));
            script = String.format("& {\n%s\n}", script);

            sizeScriptEncoded = encodeScript(script);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setup() throws IOException, InterruptedException {
        Process process = powershell(setupScriptEncoded);

        /*int ret = */process.waitFor();
//        if (ret != 0) {
//            throw new IOException(String.format("Error running windows setup script (code: %d)", ret));
//        }
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
        Process process = powershell(sizeScriptEncoded);
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

    public static String encodeScript(@NonNull String script) {
        return Base64.getEncoder()
            .encodeToString(script.getBytes(StandardCharsets.UTF_16LE));
    }

    public static Process powershell(@NonNull String encodedScript) throws IOException {
        return new ProcessBuilder()
            .command(
                "powershell.exe",
                "-NoProfile",
                "-NonInteractive",
                "-EncodedCommand", encodedScript
            )
            .inheritIO()
            .redirectError(Redirect.PIPE)
            .redirectOutput(Redirect.PIPE)
            .start();
    }

}
