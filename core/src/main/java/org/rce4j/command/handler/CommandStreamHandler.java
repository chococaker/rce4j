package org.rce4j.command.handler;

import org.rce4j.command.stream.CommandStream;

public interface CommandStreamHandler {
    void handle(String s);
    
    /**
     * Handle any errors that occur when a {@link CommandStream} calls {@link CommandStreamHandler#handle(String)}
     */
    default void onError(CommandStreamHandler source, RuntimeException e) {
    }
}
