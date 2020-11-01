package xyz.e3ndr.consoleutil.input;

import lombok.Getter;

public enum InputKey {
    ENTER(13, false, true),
    BACK_SPACE(8, false, true),
    TAB(9, false, true),

    // CONTROL(17), // TODO ?
    ALT(27),

    ESCAPE(96),
    PAGE_UP(53, true, false),
    PAGE_DOWN(54, true, false),
    END(52, true, false),
    HOME(49, true, false),
    DELETE(51, true, false),
    INSERT(50, true, false),

    LEFT(68),
    UP(65),
    RIGHT(67),
    DOWN(66);

    private @Getter boolean phantom = false;
    private @Getter boolean regular = false;
    private @Getter int code;

    private InputKey(int code) {
        this.code = code;
    }

    private InputKey(int code, boolean phantom, boolean regular) {
        this.phantom = phantom;
        this.regular = regular;
        this.code = code;
    }

}
