package xyz.e3ndr.consoleutil.ipc;

import java.io.Closeable;
import java.io.IOException;

/**
 * @apiNote Programs are responsible for implementing their own ping structure.
 **/
public interface IpcChannel extends Closeable {

    /**
     * Checks if is child.
     *
     * @return true, if is child
     */
    public boolean isChild();

    /**
     * Writes a string to the destination.
     * 
     * @apiNote                      Programs are responsible for implementing their
     *                               own ping structure.
     *
     * @param   str                  the string to write
     * 
     * @throws  IOException          Signals that an I/O exception has occurred.
     * @throws  InterruptedException the interrupted exception
     */
    public void write(String str) throws IOException, InterruptedException;

    /**
     * Reads a string from the source.
     * 
     * @apiNote                      Programs are responsible for implementing their
     *                               own ping structure.
     * 
     * @return                       the read string
     * 
     * @throws  IOException          Signals that an I/O exception has occurred.
     * @throws  InterruptedException the interrupted exception
     */
    public String read() throws IOException, InterruptedException;

}
