package xyz.e3ndr.consoleutil.ipc;

import java.io.Closeable;
import java.io.IOException;

public interface IpcChannel extends Closeable {

    public boolean isChild();

    public void write(String str) throws IOException, InterruptedException;

    public String read() throws IOException, InterruptedException;

}
