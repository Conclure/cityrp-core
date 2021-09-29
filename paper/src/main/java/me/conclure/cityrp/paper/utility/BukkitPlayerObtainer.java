package me.conclure.cityrp.paper.utility;

import me.conclure.cityrp.common.utility.PlayerObtainer;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BukkitPlayerObtainer implements PlayerObtainer<Player> {
    private final Server server;

    public BukkitPlayerObtainer(Server server) {
        this.server = server;
    }

    @Override
    public Player getByUniqueId(UUID uniqueId) {
        return this.server.getPlayer(uniqueId);
    }

    @Override
    public Player getByName(String name) {
        return this.server.getPlayer(name);
    }
}
