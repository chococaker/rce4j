package org.rce4j.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;

import java.util.logging.Logger;

public class PluginRegistrationListener implements Listener {
    @EventHandler
    public void onPluginEnable(PluginEnableEvent event) {
        Logger logger = event.getPlugin().getLogger();
    }
    
    @EventHandler
    public void onPluginDisable(PluginDisableEvent event) {
        Logger logger = event.getPlugin().getLogger();
        
    }
}
