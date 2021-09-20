package me.conclure.cityrp.position;

import com.github.benmanes.caffeine.cache.LoadingCache;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import me.conclure.cityrp.registry.Registries;
import me.conclure.cityrp.utility.CaffeineFactory;
import me.conclure.cityrp.utility.ConfigurateLoaderFactory;
import me.conclure.cityrp.utility.concurrent.TaskCoordinator;
import me.conclure.cityrp.utility.exception.RuntimeFileIOException;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.jackson.JacksonConfigurationLoader;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PositionDataManager {
    private final Path destinationDirectory;
    private final ConfigurateLoaderFactory loader;
    private final LoadingCache<Position, ReentrantLock> ioLocks;
    private final TaskCoordinator<? extends Executor> taskCoordinator;

    public PositionDataManager(Path destinationDirectory, TaskCoordinator<? extends Executor> taskCoordinator) {
        this.destinationDirectory = destinationDirectory;
        this.taskCoordinator = taskCoordinator;
        this.loader = path -> JacksonConfigurationLoader.builder()
                .source(() -> Files.newBufferedReader(path, StandardCharsets.UTF_8))
                .sink(() -> Files.newBufferedWriter(path, StandardCharsets.UTF_8))
                .build();
        this.ioLocks = CaffeineFactory.newBuilder()
                .expireAfterAccess(10, TimeUnit.MINUTES)
                .build(key -> new ReentrantLock(true));
    }

    private static <T> CompletableFuture<Void> applyAllPositionsToFuture(Function<Position,CompletableFuture<T>> function) {
        CompletableFuture<?>[] futures = Registries.POSITIONS.stream()
                .map(function)
                .toArray(CompletableFuture[]::new);
        return CompletableFuture.allOf(futures);
    }

    public CompletableFuture<Void> loadAll() {
        return PositionDataManager.applyAllPositionsToFuture(this::loadData);
    }

    public CompletableFuture<Void> saveAll() {
        return PositionDataManager.applyAllPositionsToFuture(this::saveData);
    }

    public CompletableFuture<Boolean> loadData(Position position) {
        return this.taskCoordinator.supply(() -> {
            try {
                Lock lock = this.ioLocks.get(position);
                Preconditions.checkNotNull(lock);
                ConfigurationNode node = null;

                lock.lock();
                try {
                    Path path = this.destinationDirectory.resolve(Registries.POSITIONS.getKey(position) + ".json");
                    if (Files.exists(path)) {
                        node = this.loader.loader(path).load();
                    }
                } finally {
                    lock.unlock();
                }

                if (node == null) {
                    return Boolean.FALSE;
                }

                String worldId = node.node("world").getString();

                if (worldId == null) {
                    return Boolean.FALSE;
                }

                World world = Bukkit.getWorld(UUID.fromString(worldId));

                if (world == null) {
                    return Boolean.FALSE;
                }

                double x = node.node("x").getDouble();
                double y = node.node("y").getDouble();
                double z = node.node("z").getDouble();
                float yaw = node.node("yaw").getFloat();
                float pitch = node.node("pitch").getFloat();
                position.setLocation(new ImmutableLocationBuilder()
                        .world(world)
                        .x(x)
                        .y(y)
                        .z(z)
                        .yaw(yaw)
                        .pitch(pitch)
                        .build());
            } catch (Exception e) {
                throw new RuntimeFileIOException(e);
            }
            return Boolean.TRUE;
        });
    }

    public CompletableFuture<Boolean> saveData(Position position) {
        return this.taskCoordinator.supply(() -> {
            try {
                ImmutableLocation location = position.getLocation();
                Path path = this.destinationDirectory.resolve(Registries.POSITIONS.getKey(position) + ".json");

                if (location == null) {
                    Files.deleteIfExists(path);
                    return Boolean.FALSE;
                }

                ConfigurationNode node = BasicConfigurationNode.root();
                World world = location.getWorld();

                if (world == null) {
                    return Boolean.FALSE;
                }

                node.node("world").set(world.getUID());
                node.node("x").set(location.getX());
                node.node("y").set(location.getY());
                node.node("z").set(location.getZ());
                node.node("yaw").set(location.getYaw());
                node.node("pitch").set(location.getPitch());

                Lock lock = this.ioLocks.get(position);
                Preconditions.checkNotNull(lock);

                lock.lock();
                try {
                    this.loader.loader(path).save(node);
                } finally {
                    lock.unlock();
                }
            } catch (Exception e) {
                throw new RuntimeFileIOException(e);
            }

            return Boolean.TRUE;
        });
    }
}
