package org.rce4j.module;

import org.rce4j.module.event.BackdoorEvent;
import org.rce4j.RceServer;

public interface BackdoorManager {
    BackdoorManager EMPTY = new BackdoorManager() {};
    
    default void onOpen(RceServer server) {
    }
    
    default void onClose(RceServer server) {
    }
    
    default void onEvent(BackdoorEvent event) {
    }
}
