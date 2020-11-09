package xyz.e3ndr.consoleutil;

import org.fusesource.jansi.Ansi;

import lombok.Getter;

public enum ConsoleAttribute {

    /**
     * Resets all formatting.
     */
    RESET(Ansi.Attribute.INTENSITY_BOLD_OFF, Ansi.Attribute.UNDERLINE_OFF, Ansi.Attribute.ITALIC_OFF, Ansi.Attribute.STRIKETHROUGH_OFF),
    BOLD(Ansi.Attribute.INTENSITY_BOLD),
    UNDERLINE(Ansi.Attribute.UNDERLINE),
    ITALIC(Ansi.Attribute.ITALIC),
    STRIKETHROUGH(Ansi.Attribute.STRIKETHROUGH_ON),
    CUSTOR_BLINK_FAST(Ansi.Attribute.BLINK_FAST),
    CUSTOR_BLINK_SLOW(Ansi.Attribute.BLINK_SLOW),
    CUSTOR_BLINK_OFF(Ansi.Attribute.BLINK_OFF);

    @Getter
    private Ansi.Attribute[] ansiAttributes;

    private ConsoleAttribute(Ansi.Attribute... ansiAttributes) {
        this.ansiAttributes = ansiAttributes;
    }

}
