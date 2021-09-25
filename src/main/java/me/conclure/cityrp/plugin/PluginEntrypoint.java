package me.conclure.cityrp.plugin;

import org.bukkit.Server;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.ScoreboardManager;

@Plugin(
        name = "CityRP",
        version = "1.2"
)
@ApiVersion(ApiVersion.Target.v1_16)
public class PluginEntrypoint extends JavaPlugin {
    private final Lifecycle lifecycle;

    public PluginEntrypoint() {
        Server server = this.getServer();
        PluginManager pluginManager = server.getPluginManager();
        ScoreboardManager scoreboardManager = server.getScoreboardManager();
        ServicesManager servicesManager = server.getServicesManager();
        BukkitScheduler scheduler = server.getScheduler();
        this.lifecycle = new PluginLifecycle(this, server, pluginManager, scoreboardManager, servicesManager, scheduler);
    }

    @Override
    public void onLoad() {
        this.lifecycle.load();
    }

    @Override
    public void onEnable() {
        this.lifecycle.enable();
    }

    @Override
    public void onDisable() {
        this.lifecycle.disable();
    }
}
