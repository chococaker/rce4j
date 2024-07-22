package org.rce4j.module;

import org.rce4j.exception.HttpException;
import org.rce4j.module.event.BackdoorHttpEvent;

import java.util.Collection;
import java.util.Set;

public class HttpModuleSuite extends ModuleSuite<BackdoorHttpModule, BackdoorHttpEvent> {
    public static final HttpModuleSuite EMPTY = new HttpModuleSuite() {
        @Override
        public Collection<ModulePath> getPaths() {
            return Set.of();
        }
        
        @Override
        void fire(BackdoorHttpEvent event) throws HttpException {
            throw new HttpException(404);
        }
    };
    
    public HttpModuleSuite() {
        super();
    }
    
    public HttpModuleSuite(BackdoorHttpModule... modules) {
        super(modules);
    }
    
    public HttpModuleSuite(Collection<? extends BackdoorHttpModule> modules) {
        super(modules);
    }
}
