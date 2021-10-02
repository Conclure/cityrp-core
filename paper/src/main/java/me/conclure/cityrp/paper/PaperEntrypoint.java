package me.conclure.cityrp.paper;

import me.conclure.cityrp.common.plugin.PluginLifecycle;
import me.conclure.cityrp.common.utility.concurrent.AwaitableLatch;
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
    private final AwaitableLatch enableLatch;

    public PaperEntrypoint() {
        this.enableLatch = new AwaitableLatch();
        Server server = this.getServer();
        PluginManager pluginManager = server.getPluginManager();
        ScoreboardManager scoreboardManager = server.getScoreboardManager();
        ServicesManager servicesManager = server.getServicesManager();
        BukkitScheduler scheduler = server.getScheduler();
        this.lifecycle = new PaperLifecycle(this.enableLatch, this, server, pluginManager, scoreboardManager, servicesManager, scheduler);
    }

    @Override
    public void onLoad() {
        this.lifecycle.load();
    }

    @Override
    public void onEnable() {
        try {
            this.lifecycle.enable();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            this.enableLatch.release();
        }
    }

    @Override
    public void onDisable() {
        this.lifecycle.disable();
    }
}
