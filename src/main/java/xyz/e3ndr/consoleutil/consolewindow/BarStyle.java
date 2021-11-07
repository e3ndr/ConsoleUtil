package xyz.e3ndr.consoleutil.consolewindow;

import co.casterlabs.rakurai.json.element.JsonObject;

public class BarStyle {

    /**
     * A default style that looks like this: <br>
     * {@code [■■■ ]}
     */
    public static final BarStyle SQUARE = new BarStyle('[', ']', ' ', '\u25A0');

    /**
     * A default style that looks like this: <br>
     * {@code [=== ]}
     */
    public static final BarStyle ANGLE = new BarStyle('[', ']', ' ', '=');

    /**
     * A default style that looks like this: <br>
     * {@code [===.]}
     */
    public static final BarStyle DOTS = new BarStyle('[', ']', '.', '=');

    /**
     * A default style that looks like this: <br>
     * {@code <###->}
     */
    public static final BarStyle ARROW = new BarStyle('<', '>', '-', '#');

    private char opening;
    private char closing;
    private char empty;
    private char full;

    /**
     * Instantiates a new bar style.
     *
     * @param opening the opening char
     * @param closing the closing char
     * @param empty   the char to use in empty spots
     * @param full    the chat to use in filled spots
     */
    public BarStyle(char opening, char closing, char empty, char full) {
        this.opening = opening;
        this.closing = closing;
        this.empty = empty;
        this.full = full;
    }

    /**
     * Instantiates a new bar style.
     *
     * @param empty the empty
     * @param full  the full
     */
    public BarStyle(char empty, char full) {
        this(' ', ' ', empty, full);
    }

    /**
     * Format.
     *
     * @param  progress    the progress, between 0-1
     * @param  size        the size of the bar
     * @param  showPercent whether or not to show a percentage
     * 
     * @return             the string
     */
    public String format(double progress, int size, boolean showPercent) {
        StringBuilder sb = new StringBuilder();

        size -= 2; // Sub 2, to account for opening tags
        sb.append(this.opening);

        int completion = (int) Math.round(Math.min(size, size * progress));

        for (int i = 0; i != completion; i++) {
            sb.append(this.full);
        }

        for (int i = 0; i != (size - completion); i++) {
            sb.append(this.empty);
        }

        sb.append(this.closing);

        if (showPercent) {
            sb.append(String.format(" (%.0f%%)", progress * 100));
        }

        return sb.toString();
    }

    @Deprecated
    public JsonObject serialize() {
        return new JsonObject()
            .put("opening", (int) this.opening)
            .put("closing", (int) this.closing)
            .put("empty", (int) this.empty)
            .put("full", (int) this.full);
    }

}
