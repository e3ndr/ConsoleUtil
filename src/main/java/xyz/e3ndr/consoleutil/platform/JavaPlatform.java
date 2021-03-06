package xyz.e3ndr.consoleutil.platform;

import lombok.AllArgsConstructor;
import lombok.Getter;
import xyz.e3ndr.consoleutil.platform.platforms.UnixPlatformHandler;
import xyz.e3ndr.consoleutil.platform.platforms.UnknownPlatformHandler;
import xyz.e3ndr.consoleutil.platform.platforms.WindowsPlatformHandler;

@AllArgsConstructor
public enum JavaPlatform {
    WINDOWS(new WindowsPlatformHandler()),
    UNIX(new UnixPlatformHandler()),
    UNKNOWN(new UnknownPlatformHandler());

    private @Getter PlatformHandler handler;

    public static JavaPlatform get() {
        String name = System.getProperty("os.name").toLowerCase();

        if (name.contains("windows")) {
            return WINDOWS;
        } else if (name.contains("mac") || name.contains("linux")) {
            return UNIX;
        } else {
            return UNKNOWN;
        }
    }

}
