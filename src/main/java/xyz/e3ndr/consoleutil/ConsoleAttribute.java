package xyz.e3ndr.consoleutil;

import org.fusesource.jansi.Ansi;

import lombok.Getter;

public enum ConsoleAttribute {

    /**
     * Resets all formatting.
     */
    RESET(Ansi.Attribute.INTENSITY_BOLD_OFF, Ansi.Attribute.UNDERLINE_OFF, Ansi.Attribute.ITALIC_OFF, Ansi.Attribute.STRIKETHROUGH_OFF),
    BOLD(Ansi.Attribute.INTENSITY_BOLD),
    UNDERLINE(Ansi.Attribute.INTENSITY_BOLD),
    ITALIC(Ansi.Attribute.INTENSITY_BOLD),
    STRIKETHROUGH(Ansi.Attribute.INTENSITY_BOLD);

    @Getter
    private Ansi.Attribute[] ansiAttributes;

    private ConsoleAttribute(Ansi.Attribute... ansiAttributes) {
        this.ansiAttributes = ansiAttributes;
    }

}
