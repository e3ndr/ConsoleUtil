package xyz.e3ndr.consoleutil.input;

import lombok.Getter;

public enum InputKey {
    ENTER(13, false, true),
    BACK_SPACE(8, false, true),
    TAB(9, false, true),

    // CONTROL OFFSET = 96
    CTRL_A('a' - 96, false, true),
    CTRL_B('b' - 96, false, true),
    CTRL_C('c' - 96, false, true),
    CTRL_D('d' - 96, false, true),
    CTRL_E('e' - 96, false, true),
    CTRL_F('f' - 96, false, true),
    CTRL_G('g' - 96, false, true),
    CTRL_H('h' - 96, false, true),
    CTRL_I('i' - 96, false, true),
    CTRL_J('j' - 96, false, true),
    CTRL_K('k' - 96, false, true),
    CTRL_L('l' - 96, false, true),
    CTRL_M('m' - 96, false, true),
    CTRL_N('n' - 96, false, true),
    CTRL_O('o' - 96, false, true),
    CTRL_P('p' - 96, false, true),
    CTRL_Q('q' - 96, false, true),
    CTRL_R('r' - 96, false, true),
    CTRL_S('s' - 96, false, true),
    CTRL_T('t' - 96, false, true),
    CTRL_U('u' - 96, false, true),
    CTRL_V('v' - 96, false, true),
    CTRL_W('w' - 96, false, true),
    CTRL_X('x' - 96, false, true),
    CTRL_Y('y' - 96, false, true),
    CTRL_Z('z' - 96, false, true),
    CTRL_LEFT_BRACE('{' - 96, false, true),
    CTRL_RIGHT_BRACE('}' - 96, false, true),
    CTRL_PIPE('|' - 96, false, true),

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
