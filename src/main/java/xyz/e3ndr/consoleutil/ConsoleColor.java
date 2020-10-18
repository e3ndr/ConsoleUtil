package xyz.e3ndr.consoleutil;

import org.fusesource.jansi.Ansi;

import lombok.Getter;

@Getter
public enum ConsoleColor {
    RED(false, Ansi.Color.RED),
    LIGHT_RED(true, Ansi.Color.RED),

    YELLOW(false, Ansi.Color.YELLOW),
    LIGHT_YELLOW(true, Ansi.Color.YELLOW),

    GREEN(false, Ansi.Color.GREEN),
    LIGHT_GREEN(true, Ansi.Color.GREEN),

    CYAN(false, Ansi.Color.CYAN),
    LIGHT_CYAN(true, Ansi.Color.CYAN),

    BLUE(false, Ansi.Color.BLUE),
    LIGHT_BLUE(true, Ansi.Color.BLUE),

    MAGENTA(false, Ansi.Color.MAGENTA),
    LIGHT_MAGENTA(true, Ansi.Color.MAGENTA),

    WHITE(false, Ansi.Color.WHITE),

    GREY(false, Ansi.Color.DEFAULT),
    GRAY(false, Ansi.Color.DEFAULT),
    DEFAULT(false, Ansi.Color.DEFAULT),

    DARK_GREY(true, Ansi.Color.BLACK),
    DARK_GRAY(true, Ansi.Color.BLACK),

    BLACK(false, Ansi.Color.BLACK);

    private Ansi.Color color;
    private boolean isLight;

    private ConsoleColor(boolean isLight, Ansi.Color color) {
        this.isLight = isLight;
        this.color = color;
    }

}
