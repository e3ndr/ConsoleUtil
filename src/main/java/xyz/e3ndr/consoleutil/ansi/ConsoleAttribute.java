package xyz.e3ndr.consoleutil.ansi;

import org.fusesource.jansi.Ansi;

import lombok.Getter;

public enum ConsoleAttribute {

    /**
     * Resets all formatting.
     */
    RESET(Ansi.Attribute.RESET),
    BOLD(Ansi.Attribute.INTENSITY_BOLD),
    UNDERLINE(Ansi.Attribute.UNDERLINE),
    ITALIC(Ansi.Attribute.ITALIC),
    STRIKETHROUGH(Ansi.Attribute.STRIKETHROUGH_ON),
    CURSOR_BLINK_FAST(Ansi.Attribute.BLINK_FAST),
    CURSOR_BLINK_SLOW(Ansi.Attribute.BLINK_SLOW),
    CURSOR_BLINK_OFF(Ansi.Attribute.BLINK_OFF);

    @Getter
    private String ansi;

    private ConsoleAttribute(Ansi.Attribute attribute) {
        this.ansi = new Ansi().a(attribute).toString();
    }

}
