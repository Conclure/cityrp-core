package me.conclure.cityrp.common.model.position;

import me.conclure.cityrp.common.sender.Sender;
import me.conclure.cityrp.common.utility.Key;
import org.jspecify.nullness.Nullable;

import java.util.concurrent.CompletableFuture;

public interface Position<PlatformEntity, PlatformWorld> {
    PositionInfo getInfo();

    boolean hasPermission(Sender sender);

    Key getKey();

    boolean hasLocation();

    double getX();

    double getY();

    double getZ();

    String getWorldName();

    @Nullable
    PlatformWorld getWorld();

    float getYaw();

    float getPitch();

    void configure(double x, double y, double z, PlatformWorld world, float yaw, float pitch);

    default void configure(double x, double y, double z, PlatformWorld world) {
        this.configure(x, y, z, world, 0f, 0f);
    }

    CompletableFuture<Boolean> teleportAsync(PlatformEntity entity);

    boolean teleport(PlatformEntity entity);
}
