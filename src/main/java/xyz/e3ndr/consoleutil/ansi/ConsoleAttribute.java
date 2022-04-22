package xyz.e3ndr.consoleutil.ansi;

import lombok.Getter;

public enum ConsoleAttribute {

    /**
     * Resets all formatting.
     */
    RESET("^[22m^[23m^[24m^[25m^[27m^[28m^[9m"),

    // BOLD
    // DIM/FAINT
    ITALIC("^[3m"),
    UNDERLINE(""),
    // BLINKING
    // REVERSE
    // INVISIBLE
    // STRIKE
    STRIKETHROUGH("^[9m");

    @Getter
    private String ansi;

    private ConsoleAttribute(String ansi) {
        this.ansi = ansi.replace("^", "\u001b");
    }

}
