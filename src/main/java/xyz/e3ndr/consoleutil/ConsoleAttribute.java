package xyz.e3ndr.consoleutil;

import org.fusesource.jansi.Ansi;

import lombok.Getter;

@Getter
public enum ConsoleAttribute {
    RESET(Ansi.Attribute.INTENSITY_BOLD_OFF, Ansi.Attribute.UNDERLINE_OFF, Ansi.Attribute.ITALIC_OFF, Ansi.Attribute.STRIKETHROUGH_OFF),

    BOLD(Ansi.Attribute.INTENSITY_BOLD),
    UNDERLINE(Ansi.Attribute.INTENSITY_BOLD),
    ITALIC(Ansi.Attribute.INTENSITY_BOLD),
    STRIKETHROUGH(Ansi.Attribute.INTENSITY_BOLD);

    private Ansi.Attribute[] attributes;

    private ConsoleAttribute(Ansi.Attribute... attributes) {
        this.attributes = attributes;
    }

}
