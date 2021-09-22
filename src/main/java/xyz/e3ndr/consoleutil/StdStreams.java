package xyz.e3ndr.consoleutil;

import java.io.InputStream;
import java.io.PrintStream;

import org.jetbrains.annotations.Nullable;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class StdStreams {
    public static final StdStreams CURRENT = new StdStreams(System.out, System.err, System.in);

    public @Nullable final PrintStream out;
    public @Nullable final PrintStream err;
    public @Nullable final InputStream in;

}
