package xyz.e3ndr.consoleutil.platform;

import lombok.AllArgsConstructor;
import lombok.Getter;
import xyz.e3ndr.consoleutil.platform.impl.MacPlatformHandler;
import xyz.e3ndr.consoleutil.platform.impl.UnixPlatformHandler;
import xyz.e3ndr.consoleutil.platform.impl.UnknownPlatformHandler;
import xyz.e3ndr.consoleutil.platform.impl.WindowsPlatformHandler;

@AllArgsConstructor
public enum JavaPlatform {
    WINDOWS(new WindowsPlatformHandler()),
    UNIX(new UnixPlatformHandler()),
    MAC(new MacPlatformHandler()),
    UNKNOWN(new UnknownPlatformHandler());

    private @Getter PlatformHandler handler;

    public static JavaPlatform get() {
        String name = System.getProperty("os.name").toLowerCase();

        if (name.contains("windows")) {
            return WINDOWS;
        } else if (name.contains("mac")) {
            return MAC;
        } else if (name.contains("linux")) {
            return UNIX;
        } else {
            return UNKNOWN;
        }
    }

}
