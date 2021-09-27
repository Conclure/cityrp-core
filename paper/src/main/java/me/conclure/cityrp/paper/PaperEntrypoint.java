package me.conclure.cityrp.paper;

import me.conclure.cityrp.common.plugin.PluginLifecycle;
import org.bukkit.Server;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.ScoreboardManager;

@Plugin(
        name = PluginLifecycle.PLUGIN_NAME,
        version = "1.2"
)
@ApiVersion(ApiVersion.Target.v1_16)
public class PaperEntrypoint extends JavaPlugin {
    private final PluginLifecycle lifecycle;

    public PaperEntrypoint() {
        Server server = this.getServer();
        PluginManager pluginManager = server.getPluginManager();
        ScoreboardManager scoreboardManager = server.getScoreboardManager();
        ServicesManager servicesManager = server.getServicesManager();
        BukkitScheduler scheduler = server.getScheduler();
        this.lifecycle = new PaperLifecycle(this, server, pluginManager, scoreboardManager, servicesManager, scheduler);
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
