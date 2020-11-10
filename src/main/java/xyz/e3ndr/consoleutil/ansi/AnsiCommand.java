package xyz.e3ndr.consoleutil.ansi;

import lombok.Getter;

@Getter
public enum AnsiCommand {
    CURSOR_UP("%dA"),
    CURSOR_DOWN("%dB"),
    CURSOR_RIGHT("%dC"),
    CURSOR_LEFT("%dD"),
    CURSOR_SAVE("s"),
    CURSOR_RESTORE("u"),
    CURSOR_MOVE("%d;%dH"),

    ERASE_DISPLAY("2J"),
    ERASE_LINE("K");

    private String sequence;

    private AnsiCommand(String sequence) {
        this.sequence = "\u001b[" + sequence;
    }

}
