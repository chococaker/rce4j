package org.rce4j.command.stream;

import org.bukkit.plugin.Plugin;
import org.rce4j.PluginUpdateListener;
import org.rce4j.util.ConcurrentHashSet;

import java.util.Collection;
import java.util.Iterator;

public class SpigotJLoggerCommandStream
        extends JLoggerCommandStream
        implements PluginUpdateListener {
    private final Collection<Plugin> listeningPlugins = new ConcurrentHashSet<>();
    
    @Override
    protected void onClose() {
        for (Iterator<Plugin> it = listeningPlugins.iterator(); it.hasNext();) {
            Plugin plugin = it.next();
            plugin.getLogger().removeHandler(getHandler());
            
            it.remove();
        }
    }
    
    @Override
    public void onPluginEnable(Plugin plugin) {
        listeningPlugins.add(plugin);
    }
    
    @Override
    public void onPluginDisable(Plugin plugin) {
        listeningPlugins.remove(plugin);
        plugin.getLogger().removeHandler(getHandler());
    }
}
