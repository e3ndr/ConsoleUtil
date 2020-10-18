package xyz.e3ndr.consoleutil;

import java.io.IOException;

import lombok.Getter;
import lombok.NonNull;
import xyz.e3ndr.consoleutil.platform.JavaPlatform;
import xyz.e3ndr.consoleutil.platform.PlatformHandler;
import xyz.e3ndr.fastloggingframework.logging.FastLogger;

public class ConsoleUtil {
    private static @Getter final JavaPlatform platform = JavaPlatform.get();
    private static @Getter @NonNull PlatformHandler handler;
    private static FastLogger logger = new FastLogger();

    static {
        if (platform == JavaPlatform.IDE) {
            logger.warn("An IDE enviroment was detected, defaulting to a console size of 40,10");
            logger.warn("If this is not an IDE enviroment make an issue report.");
        } else if (platform == JavaPlatform.UNKNOWN) {
            logger.warn("Could not detect system type, defaulting to a console size of 40,10");
            logger.warn("Please report the system type \"%s\" to the developers (Make an issue report).", System.getProperty("os.name"));
        }

        handler = platform.getHandler(); // Allows programs to manually set a handler.
    }

    public static void clearConsole() throws InterruptedException, IOException {
        handler.clearConsole();
    }

    public static void setTitle(@NonNull String title) throws InterruptedException, IOException {
        handler.setTitle(title);
    }

    public static void setSize(int width, int height) throws InterruptedException, IOException {
        handler.setSize(width, height);
    }

    @Deprecated
    public static void setHandler(@NonNull PlatformHandler newHandler) {
        handler = newHandler;
    }

}
