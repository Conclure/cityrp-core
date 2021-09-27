package me.conclure.cityrp.common.position;

import me.conclure.cityrp.common.sender.Sender;
import me.conclure.cityrp.common.utility.Key;
import org.jspecify.nullness.Nullable;

import java.util.concurrent.CompletableFuture;

public interface Position<E,W> {
    PositionInfo getInfo();

    boolean hasPermission(Sender<?> sender);

    Key getKey();

    boolean hasLocation();

    double getX();

    double getY();

    double getZ();

    String getWorldName();

    @Nullable
    W getWorld();

    float getYaw();

    float getPitch();

    void configure(double x, double y, double z, W world, float yaw, float pitch);

    default void configure(double x, double y, double z, W world) {
        this.configure(x,y,z,world,0f,0f);
    }

    CompletableFuture<Boolean> teleport(E entity);
}
