package xyz.e3ndr.consoleutil.ansi;

import lombok.Getter;

public enum ConsoleAttribute {

    // @formatter:off
    /**
     * Resets all formatting.
     */
    RESET("^[0m"),

    BOLD("^[1m"), BOLD_OFF("^[22m"),
    DIM("^[2m"), DIM_OFF("^[22m"),
    ITALIC("^[3m"), ITALIC_OFF("^[23m"),
    UNDERLINE("^[4m"), UNDERLINE_OFF("^[24m"),
    BLINKING("^[5m"), BLINKING_OFF("^[25m"),
    REVERSE("^[7m"), REVERSE_OFF("^[27m"),
    INVISIBLE("^[8m"), INVISIBLE_OFF("^[28m"),
    STRIKETHROUGH("^[9m"), STRIKETHROUGH_OFF("^[29m"),
    
    ; 
    // @formatter:on

    @Getter
    private String ansi;

    private ConsoleAttribute(String ansi) {
        this.ansi = ansi.replace("^", "\u001b");
    }

}
