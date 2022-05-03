package xyz.e3ndr.consoleutil.ansi;

import lombok.Getter;

@Getter
public enum ConsoleColor {
    // @formatter:off

    BLACK         (0),
    DARK_GREY     (BLACK),
    
    RED           (1),
    LIGHT_RED     (RED),

    GREEN         (2),
    LIGHT_GREEN   (GREEN),

    YELLOW        (3),
    LIGHT_YELLOW  (YELLOW),

    BLUE          (4),
    LIGHT_BLUE    (BLUE),

    MAGENTA       (5),
    LIGHT_MAGENTA (MAGENTA),
    
    CYAN          (6),
    LIGHT_CYAN    (CYAN),

    GRAY          (7),
    WHITE         (GRAY),

    TERMINAL_DEFAULT       (9),
    TERMINAL_DEFAULT_LIGHT (TERMINAL_DEFAULT),

    ;
    // @formatter:on

    private int value;

    private String background;
    private String foreground;
    private boolean light;

    private ConsoleColor alternate;

    private ConsoleColor(int value) {
        this(false, value);
    }

    private ConsoleColor(ConsoleColor color) {
        this(true, color.value);

        color.alternate = this;
        this.alternate = color;
    }

    private ConsoleColor(boolean isLight, int value) {
        this.light = isLight;
        this.value = value;

        if (isLight) {
            this.foreground = String.format("\u001b[%dm", value + 90);
            this.background = String.format("\u001b[%dm", value + 100);
        } else {
            this.foreground = String.format("\u001b[%dm", value + 30);
            this.background = String.format("\u001b[%dm", value + 40);
        }
    }

    /**
     * Color map:
     * https://user-images.githubusercontent.com/995050/47952855-ecb12480-df75-11e8-89d4-ac26c50e80b9.png
     */
    public static String get8BitColor(int value) {
        return String.format("\u001b[38;5;%dm", value);
    }

    /**
     * Color map:
     * https://user-images.githubusercontent.com/995050/47952855-ecb12480-df75-11e8-89d4-ac26c50e80b9.png
     */
    public static String get8BitBackgroundColor(int value) {
        return String.format("\u001b[48;5;%dm", value);
    }

    public static String get24BitColor(int r, int g, int b) {
        return String.format("\u001b[38;2;%d;%d;%dm", r, g, b);
    }

    public static String get24BitBackgroundColor(int r, int g, int b) {
        return String.format("\u001b[48;2;%d;%d;%dm", r, g, b);
    }

}
