package xyz.e3ndr.consoleutil.input;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import jline.internal.NonBlockingInputStream;
import lombok.NonNull;
import xyz.e3ndr.consoleutil.ConsoleUtil;

public class KeyHook {
    private static final int ANSI_ESCAPE = 27;
    private static final int ANSI_START = 91;
    private static final int PHANTOM = 126;
    private static final long PEEK = 15;

    private static NonBlockingInputStream in;
    private static Set<KeyListener> listeners = new HashSet<>();

    static {
        try {
            in = new NonBlockingInputStream(ConsoleUtil.getJLine().wrapInIfNeeded(System.in), true);

            Thread t = new Thread() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            read();
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
                int next = in.peek(PEEK);

                if (next == -2) {
                    broadcast(InputKey.ESCAPE);
                } else if (next == ANSI_START) {
                    in.read(); // Clear the start.

                    int code = in.read();

                    for (InputKey key : InputKey.values()) {
                        if (key.getCode() == code) {
                            if (!key.isPhantom() || (in.peek(PEEK) == PHANTOM)) {
                                if (key.isPhantom()) {
                                    in.read(); // Clear the phantom.
                                }

                                broadcast(key);
                                return;
                            }
                        }
                    }

                    // Not a valid code, send the keys to the listeners instead.
                    broadcast(InputKey.ALT);
                    broadcast(ANSI_START);
                } else {
                    broadcast(InputKey.ALT);
                }
            } else {
                broadcast(read);
            }
        } else {
            throw new IOException("End of stream has been reached.");
        }
    }

    private static void broadcast(int key) {
        Iterator<KeyListener> it = listeners.iterator();

        while (it.hasNext()) {
            it.next().onKey(key);
        }
    }

    private static void broadcast(InputKey key) {
        Iterator<KeyListener> it = listeners.iterator();

        while (it.hasNext()) {
            it.next().onKey(key);
        }
    }

}
