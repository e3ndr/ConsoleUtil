package xyz.e3ndr.consoleutil.consolewindow.impl;

import java.io.IOException;

import xyz.e3ndr.consoleutil.ipc.IpcChannel;
import xyz.e3ndr.consoleutil.ipc.MemoryMappedIpc;

public class RemoteConsoleWindowInstance {

    public static void main(String[] args) throws IOException, InterruptedException {
        String ipcIc = args[0];
        IpcChannel childIpc = MemoryMappedIpc.startChildIpc(ipcIc);

        System.out.println(childIpc.read());
        childIpc.write("Hello from client!");
        System.out.println(childIpc.read());

    }

}
