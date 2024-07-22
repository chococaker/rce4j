package org.rce4j.command;

import org.rce4j.command.stream.CommandStream;
import org.rce4j.command.stream.SystemCommandStream;

public class EmptyCommandLine implements CommandLine {
    private final CommandStream commandStream = new SystemCommandStream();
    
    @Override
    public void dispatchCommand(String command) {
    
    }
    
    @Override
    public void dispatchCommand(String command, String as) {
    
    }
    
    @Override
    public CommandStream getCommandStream() {
        return commandStream;
    }
}
