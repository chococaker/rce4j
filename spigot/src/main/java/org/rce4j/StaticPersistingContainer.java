package org.rce4j;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;
import org.rce4j.event.PluginRegistrationListener;
import org.rce4j.util.ConcurrentHashSet;

import java.util.Set;

import static org.rce4j.SpigotUtil.getEnabledPlugin;

/**
 * Persists and re-registers certain resources (e.g. logging listeners) even when a plugin is disabled.
 */
public final class StaticPersistingContainer {
    private StaticPersistingContainer() {
    }
    
    private static Plugin currentPlugin = getEnabledPlugin();
    
    private static final Set<PluginUpdateListener> pluginUpdateListeners = new ConcurrentHashSet<>();
    private static final Set<Plugin> pluginsWithListeners = new ConcurrentHashSet<>();
    
    // Dummy to initialise
    public static void initialise() {
    }
    
    public static void runTaskOnBukkitThread(Runnable runnable) {
        Bukkit.getScheduler().runTask(currentPlugin, runnable);
    }
    
    public static void applyRegistrationListener() {
        boolean hasListener = false;
        
        for (RegisteredListener listener : PluginEnableEvent.getHandlerList().getRegisteredListeners()) {
            if (listener.getListener() instanceof PluginRegistrationListener) {
                hasListener = true;
                break;
            }
        }
        
        if (!hasListener) {
            Bukkit.getPluginManager().registerEvents(new PluginRegistrationListener(), currentPlugin);
        }
    }
    
    public static void addPluginUpdateListener(PluginUpdateListener listener) {
        pluginUpdateListeners.add(listener);
    }
    
    private static final class PluginRegistrationHandler implements Listener {
        @EventHandler
        public void onPluginEnable(PluginEnableEvent event) {
            if (!pluginsWithListeners.add(event.getPlugin())) {
                return;
            }
            
            for (PluginUpdateListener listener : pluginUpdateListeners) {
                listener.onPluginEnable(event.getPlugin());
            }
        }
        
        @EventHandler
        public void onPluginDisable(PluginDisableEvent event) {
            pluginsWithListeners.remove(event.getPlugin());
            
            for (PluginUpdateListener listener : pluginUpdateListeners) {
                listener.onPluginDisable(event.getPlugin());
            }
        }
    }
}
