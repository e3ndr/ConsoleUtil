package xyz.e3ndr.consoleutil.input;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import xyz.e3ndr.consoleutil.input.impl.CurrentKeyHook;

public class KeyHook {
    public static final KeyHook CURRENT = new CurrentKeyHook();

    protected @Getter @Setter boolean ignoringInterrupt = true;
    protected Set<KeyListener> listeners = new HashSet<>();

    public boolean addListener(@NonNull KeyListener listener) {
        return listeners.add(listener);
    }

    public boolean removeListener(@NonNull KeyListener listener) {
        return listeners.remove(listener);
    }

}
