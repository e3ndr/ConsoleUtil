package xyz.e3ndr.consoleutil.input;

import lombok.Getter;

public enum InputKey {
    ENTER(13),
    BACK_SPACE(8),
    TAB(9),

    // CONTROL(17), // TODO ?
    ALT(27),

    ESCAPE(96),
    PAGE_UP(53, true),
    PAGE_DOWN(54, true),
    END(52, true),
    HOME(49, true),
    DELETE(51, true),
    INSERT(50, true),

    LEFT(68),
    UP(65),
    RIGHT(67),
    DOWN(66);

    private @Getter boolean phantom = false;
    private @Getter int code;

    private InputKey(int code) {
        this.code = code;
    }

    private InputKey(int code, boolean phantom) {
        this.phantom = phantom;
        this.code = code;
    }

}
