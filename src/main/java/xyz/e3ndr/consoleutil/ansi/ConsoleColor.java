package xyz.e3ndr.consoleutil.ansi;

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

    GRAY(false, Ansi.Color.WHITE),
    WHITE(true, Ansi.Color.WHITE),

    DARK_GRAY(true, Ansi.Color.BLACK),
    BLACK(false, Ansi.Color.BLACK);

    private String background;
    private String foreground;
    private boolean light;

    private ConsoleColor(boolean isLight, Ansi.Color ansiColor) {
        this.light = isLight;

        if (isLight) {
            this.background = new Ansi().bgBright(ansiColor).toString();
            this.foreground = new Ansi().fgBright(ansiColor).toString();
        } else {
            this.background = new Ansi().bg(ansiColor).toString();
            this.foreground = new Ansi().fg(ansiColor).toString();
        }
    }

}
