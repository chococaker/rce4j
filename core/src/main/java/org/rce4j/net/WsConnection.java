package org.rce4j.net;

import net.chococaker.jjason.JsonObject;
import org.rce4j.module.ModulePath;

import java.io.Closeable;
import java.net.InetAddress;

public interface WsConnection extends Closeable {
    String getId();
    
    ModulePath getPath();
    
    InetAddress getClient();
    
    void close();
    
    void send(String s);
    
    default void send(JsonObject json) {
        send(json.toString());
    }
}
