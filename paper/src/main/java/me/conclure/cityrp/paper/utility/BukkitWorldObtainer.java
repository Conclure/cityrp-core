package me.conclure.cityrp.paper.utility;

import me.conclure.cityrp.common.utility.WorldObtainer;
import org.bukkit.Server;
import org.bukkit.World;

import java.util.UUID;

public class BukkitWorldObtainer implements WorldObtainer<World> {
    private final Server server;

    public BukkitWorldObtainer(Server server) {
        this.server = server;
    }

    @Override
    public World getByName(String name) {
        return this.server.getWorld(name);
    }

    @Override
    public World getById(UUID uniqueId) {
        return this.server.getWorld(uniqueId);
    }
}
