package xyz.e3ndr.consoleutil;

import java.io.InputStream;
import java.io.PrintStream;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class StdStreams {
    public static final StdStreams CURRENT = new StdStreams(System.out, System.err, System.in);

    public final PrintStream out;
    public final PrintStream err;
    public final InputStream in;

}
