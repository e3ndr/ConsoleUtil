package xyz.e3ndr.consoleutil.std;

import java.io.InputStream;
import java.io.OutputStream;

import org.jetbrains.annotations.Nullable;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class StdProcessStreams {
    public @Nullable final InputStream out;
    public @Nullable final InputStream err;
    public @Nullable final OutputStream in;

}
