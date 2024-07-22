package org.rce4j;

import org.rce4j.io.Openable;
import org.rce4j.module.HttpModuleSuite;
import org.rce4j.module.ModuleManager;
import org.rce4j.module.WsModuleSuite;

import java.io.Closeable;

public abstract class RceServer implements Openable, Closeable {
    public RceServer(int port, HttpModuleSuite httpModules) {
        this.port = port;
        this.moduleManager = new ModuleManager(httpModules);
    }
    
    public RceServer(int port, WsModuleSuite wsModules) {
        this.port = port;
        this.moduleManager = new ModuleManager(wsModules);
    }
    
    public RceServer(int port, HttpModuleSuite httpModules, WsModuleSuite wsModules) {
        this.port = port;
        this.moduleManager = new ModuleManager(httpModules, wsModules);
    }
    
    public RceServer(int port, WsModuleSuite wsModules, HttpModuleSuite httpModules) {
        this.port = port;
        this.moduleManager = new ModuleManager(httpModules, wsModules);
    }
    
    private final int port;
    
    private final ModuleManager moduleManager;
    
    @Override
    public abstract void open();
    
    @Override
    public abstract void close();
    
    public int getPort() {
        return port;
    }
    
    public ModuleManager getModuleManager() {
        return moduleManager;
    }
}
