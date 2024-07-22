package org.rce4j.command;

import org.rce4j.command.stream.CommandStream;

public interface CommandLine {
    /**
     * Dispatched command assumed to be executed by an admin user.
     */
    void dispatchCommand(String command);
    
    void dispatchCommand(String command, String as);
    
    CommandStream getCommandStream();
}
