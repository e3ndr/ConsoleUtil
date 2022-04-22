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
    
    /**
     * @ImplNote This is terminal specific.
     */
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

}
