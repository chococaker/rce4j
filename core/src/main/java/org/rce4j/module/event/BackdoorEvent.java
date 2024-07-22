package org.rce4j.module.event;

import net.chococaker.jjason.JsonObject;
import org.rce4j.RceServer;
import org.rce4j.module.ModulePath;

import java.net.InetAddress;

public sealed interface BackdoorEvent permits BackdoorHttpEvent, BackdoorWsEvent {
    ModulePath getPath();
    
    JsonObject getBody();
    
    RceServer getServer();
    
    InetAddress getClient();
}
