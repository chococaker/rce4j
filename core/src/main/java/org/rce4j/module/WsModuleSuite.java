package org.rce4j.module;

import org.rce4j.exception.HttpException;
import org.rce4j.module.event.BackdoorWsEvent;

import java.util.Collection;
import java.util.Set;

public class WsModuleSuite extends ModuleSuite<BackdoorWsModule, BackdoorWsEvent> {
    public static final WsModuleSuite EMPTY = new WsModuleSuite() {
        @Override
        public Collection<ModulePath> getPaths() {
            return Set.of();
        }
        
        @Override
        void fire(BackdoorWsEvent event) throws HttpException {
            throw new HttpException(404);
        }
    };
    
    public WsModuleSuite() {
        super();
    }
    
    public WsModuleSuite(BackdoorWsModule... modules) {
        super(modules);
    }
    
    public WsModuleSuite(Collection<? extends BackdoorWsModule> modules) {
        super(modules);
    }
}
