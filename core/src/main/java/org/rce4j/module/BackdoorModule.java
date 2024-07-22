package org.rce4j.module;

import org.rce4j.module.event.BackdoorEvent;

import java.io.Closeable;

public interface BackdoorModule<E extends BackdoorEvent> extends Closeable {
    String getName();
    
    ModulePath getPath();
    
    void on(E event);
    
    default void close() {}
}
