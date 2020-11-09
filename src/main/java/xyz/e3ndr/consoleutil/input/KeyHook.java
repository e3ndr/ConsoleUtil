package xyz.e3ndr.consoleutil.input;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import jline.internal.NonBlockingInputStream;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import xyz.e3ndr.consoleutil.ConsoleUtil;

public class KeyHook {
    private static final int CTRL_MASK = 0b11100000;
    private static final int ANSI_ESCAPE = 27; // Also ALT
    private static final int ANSI_START = 91;
    private static final int PHANTOM = 126;

    private static final long PEEK_TIME = 15;

    private static @Getter @Setter boolean ignoringInterrupt = true;
    private static Set<KeyListener> listeners = new HashSet<>();
    private static NonBlockingInputStream in;

    static {
        try {
            in = new NonBlockingInputStream(ConsoleUtil.getJLine().wrapInIfNeeded(System.in), true);

            Thread t = new Thread() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            // Ensures that another thread doesn't change ignoringInterrupt
                            // before we can re-enable it below.
                            boolean ignoreInterrupt = ignoringInterrupt;

                            if (ignoreInterrupt) {
                                ConsoleUtil.getJLine().disableInterruptCharacter();
                            }

                            read();

                            if (ignoreInterrupt) {
                                ConsoleUtil.getJLine().enableInterruptCharacter();
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };

            t.setName("ConsoleUtil - Key Listener Thread");
            t.setDaemon(true);
            t.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean addListener(@NonNull KeyListener listener) {
        return listeners.add(listener);
    }

    public static boolean removeListener(@NonNull KeyListener listener) {
        return listeners.remove(listener);
    }

    private static void read() throws IOException {
        int read = in.read();

        if (read != -1) {
            if (read == ANSI_ESCAPE) {
                int next = in.peek(PEEK_TIME);

                if (next == -2) {
                    broadcast(InputKey.ESCAPE);
                } else if (next == ANSI_START) {
                    in.read(); // Clear the start.

                    int code = in.read();

                    for (InputKey key : InputKey.values()) {
                        if (key.getCode() == code) {
                            if (!key.isPhantom() || (in.peek(PEEK_TIME) == PHANTOM)) {
                                if (key.isPhantom()) {
                                    in.read(); // Clear the phantom.
                                }

                                broadcast(key);

                                return;
                            }
                        }
                    }
                } else { // It's a normal sequence, e.g ALT+A
                    in.read(); // Clear the start.

                    if ((next & CTRL_MASK) == 0) {
                        broadcast(next + 96, true, true);
                    } else {
                        broadcast(next, true, false);
                    }
                }
            } else {
                for (InputKey key : InputKey.values()) {
                    if (key.isRegular() && (key.getCode() == read)) {
                        broadcast(key);

                        return;
                    }
                }

                if ((read & CTRL_MASK) == 0) {
                    broadcast(read + 96, false, true);
                } else {
                    broadcast(read, false, false);
                }
            }
        } else {
            throw new IOException("End of stream has been reached.");
        }
    }

    private static void broadcast(int key, boolean alt, boolean control) {
        Iterator<KeyListener> it = listeners.iterator();

        while (it.hasNext()) {
            it.next().onKey((char) key, alt, control);
        }
    }

    private static void broadcast(InputKey key) {
        Iterator<KeyListener> it = listeners.iterator();

        while (it.hasNext()) {
            it.next().onKey(key);
        }
    }

}
