package xyz.e3ndr.consoleutil.input;

public interface KeyListener {

    /**
     * On key.
     *
     * @param key     the character that was typed
     * @param alt     if alt was pressed
     * @param control if control was pressed
     */
    public void onKey(char key, boolean alt, boolean control);

    /**
     * On key.
     *
     * @param key the key pressed
     */
    public void onKey(InputKey key);

}
