package org.rce4j;

import org.bukkit.plugin.Plugin;

public interface PluginUpdateListener {
    default void onPluginEnable(Plugin plugin) {}
    
    default void onPluginDisable(Plugin plugin) {}
}
