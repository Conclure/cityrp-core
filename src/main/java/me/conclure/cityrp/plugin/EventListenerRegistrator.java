package me.conclure.cityrp.plugin;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class EventListenerRegistrator {
    private final PluginManager pluginManager;
    private final Plugin plugin;

    public EventListenerRegistrator(PluginManager pluginManager, Plugin plugin) {
        this.pluginManager = pluginManager;
        this.plugin = plugin;
    }

    public void register(Listener listener) {
        this.pluginManager.registerEvents(listener, this.plugin);
    }
}
