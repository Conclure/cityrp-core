package me.conclure.cityrp.paper.position;

import me.conclure.cityrp.common.model.position.AbstractPosition;
import me.conclure.cityrp.common.model.position.PositionInfo;
import me.conclure.cityrp.common.utility.Key;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jspecify.nullness.Nullable;

import java.lang.ref.WeakReference;
import java.util.concurrent.CompletableFuture;

public class BukkitPosition extends AbstractPosition<Entity, World> {
    private volatile WeakReference<World> worldReferent;

    public BukkitPosition(Key key, PositionInfo positionInfo) {
        super(key, positionInfo);
    }

    @Nullable
    private Location toLocation() {
        World world = this.worldReferent.get();

        if (world == null) {
            return null;
        }

        return new Location(world, this.getX(), this.getY(), this.getZ(), this.getPitch(), this.getYaw());
    }

    @Override
    public World getWorld() {
        return this.worldReferent.get();
    }

    @Override
    public CompletableFuture<Boolean> teleportAsync(Entity entity) {
        if (!this.hasLocation()) {
            return CompletableFuture.completedFuture(Boolean.FALSE);
        }

        Location location = this.toLocation();

        if (location == null) {
            return CompletableFuture.completedFuture(Boolean.FALSE);
        }

        return entity.teleportAsync(location, PlayerTeleportEvent.TeleportCause.PLUGIN);
    }

    @Override
    public boolean teleport(Entity entity) {
        if (!this.hasLocation()) {
            return false;
        }

        Location location = this.toLocation();

        if (location == null) {
            return false;
        }

        return entity.teleport(location, PlayerTeleportEvent.TeleportCause.PLUGIN);
    }

    @Override
    public void configure(double x, double y, double z, World world, float yaw, float pitch) {
        super.configure(x, y, z, world, yaw, pitch);
        this.worldReferent = new WeakReference<>(world);
    }

    @Override
    protected String parseWorldName(World world) {
        return world.getName();
    }
}
