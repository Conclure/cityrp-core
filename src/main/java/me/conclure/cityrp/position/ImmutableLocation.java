package me.conclure.cityrp.position;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;

import java.lang.ref.WeakReference;
import java.util.concurrent.CompletableFuture;

public class ImmutableLocation {
    private final double x, y, z;
    private final float yaw, pitch;
    private final WeakReference<World> world;

    public ImmutableLocation(double x, double y, double z, float yaw, float pitch, World world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.world = new WeakReference<>(world);
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public float getPitch() {
        return this.pitch;
    }

    public float getYaw() {
        return this.yaw;
    }

    public World getWorld() {
        return this.world.get();
    }

    public boolean isValid() {
        return this.world.get() != null;
    }

    public CompletableFuture<Boolean> teleport(Entity entity) {
        World world = this.world.get();
        if (world == null) {
            return CompletableFuture.completedFuture(Boolean.FALSE);
        }
        return entity.teleportAsync(new Location(world,this.x,this.y,this.z,this.yaw,this.pitch));
    }
}
