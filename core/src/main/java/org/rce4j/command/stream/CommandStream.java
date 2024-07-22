package org.rce4j.command.stream;

import org.rce4j.command.handler.CommandStreamHandler;
import org.rce4j.io.ClosedStreamException;

import java.io.InputStream;

/**
 * A pseudo-{@link InputStream} that retrieves lines from a console as it outputs.
 */
public interface CommandStream extends AutoCloseable {
    /**
     * Blocks the thread until the next line is outputted, then returns that line.
     */
    String nextLine() throws ClosedStreamException;

    void addHandler(CommandStreamHandler handler);
}
