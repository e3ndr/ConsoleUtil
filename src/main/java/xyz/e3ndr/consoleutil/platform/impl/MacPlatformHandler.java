package xyz.e3ndr.consoleutil.platform.impl;

import java.io.IOException;

import lombok.NonNull;
import xyz.e3ndr.consoleutil.platform.PlatformHandler;

public class MacPlatformHandler implements PlatformHandler {

    @Override
    public void clearConsole() throws IOException, InterruptedException {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    @Override
    public void setTitle(@NonNull String title) throws IOException, InterruptedException {
        System.out.printf("\033]0;%s\007", title);
        System.out.flush();
    }

    @Override
    public void setSize(int width, int height) throws IOException, InterruptedException {
        System.out.printf("\033[8;%d;%d6", width, height);
        System.out.flush();
    }

    @Override
    public void startConsoleWindow(String cmdLine) throws IOException, InterruptedException {
    	cmdLine = String.format(
			"tell application \"Terminal\" to do script \"%s && exit\"",
				cmdLine
				.replace("\"", "\\\"")
				.replace("'", "\\'")
		);
    	
        new ProcessBuilder("osascript", "-e", cmdLine).start();
    }

}
