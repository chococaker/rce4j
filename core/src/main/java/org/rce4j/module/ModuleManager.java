package org.rce4j.module;

import org.rce4j.exception.HttpException;
import org.rce4j.module.event.BackdoorEvent;
import org.rce4j.module.event.BackdoorHttpEvent;
import org.rce4j.module.event.BackdoorWsEvent;

public final class ModuleManager {
    public ModuleManager() {
        this(HttpModuleSuite.EMPTY, WsModuleSuite.EMPTY);
    }
    
    public ModuleManager(HttpModuleSuite httpModules) {
        this(httpModules, WsModuleSuite.EMPTY);
    }
    
    public ModuleManager(WsModuleSuite wsModules) {
        this(HttpModuleSuite.EMPTY, wsModules);
    }
    
    public ModuleManager(HttpModuleSuite httpModules, WsModuleSuite wsModules) {
        this.httpModules = httpModules;
        this.wsModules = wsModules;
    }
    
    private final HttpModuleSuite httpModules;
    private final WsModuleSuite wsModules;
    
    public HttpModuleSuite getHttpModules() {
        return httpModules;
    }
    
    public WsModuleSuite getWsModules() {
        return wsModules;
    }
    
    public void fire(BackdoorEvent event) throws HttpException {
        if (event instanceof BackdoorHttpEvent httpEvent) {
            if (!httpEvent.hasCompleted())
                httpModules.fire(httpEvent);
        } else if (event instanceof BackdoorWsEvent wsEvent) {
            wsModules.fire(wsEvent);
        } else {
            throw new RuntimeException("Unexpected class implementing " + BackdoorEvent.class.getName() + ": " + event.getClass().getName());
        }
    }
}
