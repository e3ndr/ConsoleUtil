package xyz.e3ndr.consoleutil.platform;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import xyz.e3ndr.consoleutil.platform.platforms.UnknownPlatformHandler;
import xyz.e3ndr.consoleutil.platform.platforms.WindowsPlatformHandler;

@AllArgsConstructor
public enum JavaPlatform {
    WINDOWS(new WindowsPlatformHandler()),
    UNIX(new UnknownPlatformHandler()),
    IDE(new UnknownPlatformHandler()),
    UNKNOWN(new UnknownPlatformHandler());

    private @Getter PlatformHandler handler;

    public static JavaPlatform get() {
        if (isIDEEnviroment()) {
            return IDE;
        } else {
            String name = System.getProperty("os.name").toLowerCase();

            if (name.contains("windows")) {
                return WINDOWS;
            } else if (name.contains("linux") || name.contains("mac")) {
                return UNIX;
            } else {
                return UNKNOWN;
            }
        }
    }

    private static boolean isIDEEnviroment() {
        File executable = new File(JavaPlatform.class.getProtectionDomain().getCodeSource().getLocation().getPath());

        // We return false if we find the manifest file, otherwise we return true.
        try (ZipFile zip = new ZipFile(executable)) {
            return zip.getEntry("META-INF/MANIFEST.MF") == null;
        } catch (IOException e) {
            return true;
        }
    }

}
