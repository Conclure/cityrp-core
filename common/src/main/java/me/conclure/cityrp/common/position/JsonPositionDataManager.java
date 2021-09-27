package me.conclure.cityrp.common.position;

import com.github.benmanes.caffeine.cache.LoadingCache;
import com.google.common.base.Preconditions;
import me.conclure.cityrp.common.utility.WorldObtainer;
import me.conclure.cityrp.common.utility.CaffeineFactory;
import me.conclure.cityrp.common.utility.ConfigurateLoaderFactory;
import me.conclure.cityrp.common.utility.concurrent.TaskCoordinator;
import me.conclure.cityrp.common.utility.exception.RuntimeFileIOException;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.jackson.JacksonConfigurationLoader;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

public class JsonPositionDataManager<E,W> implements PositionDataManager<E,W> {
    private final PositionRegistry<E,W> positionRegistry;
    private final WorldObtainer<W> worldObtainer;
    private final Path destinationDirectory;
    private final ConfigurateLoaderFactory loader;
    private final LoadingCache<Position<E,W>, ReentrantLock> ioLocks;
    private final TaskCoordinator<? extends Executor> taskCoordinator;

    public JsonPositionDataManager(
            PositionRegistry<E, W> positionRegistry,
            WorldObtainer<W> worldObtainer,
            Path destinationDirectory,
            TaskCoordinator<? extends Executor> taskCoordinator
    ) {
        this.positionRegistry = positionRegistry;
        this.worldObtainer = worldObtainer;
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

    private CompletableFuture<Void> applyAllPositionsToFuture(Function<Position<E,W>, CompletableFuture<?>> function) {
        CompletableFuture<?>[] futures = this.positionRegistry.stream()
                .map(function)
                .toArray(CompletableFuture[]::new);
        return CompletableFuture.allOf(futures);
    }

    @Override
    public CompletableFuture<Void> loadAll() {
        return this.applyAllPositionsToFuture(this::loadData);
    }

    @Override
    public CompletableFuture<Void> saveAll() {
        return this.applyAllPositionsToFuture(this::saveData);
    }

    @Override
    public CompletableFuture<Boolean> loadData(Position<E,W> position) {
        return this.taskCoordinator.supply(() -> {
            try {
                Lock lock = this.ioLocks.get(position);
                Preconditions.checkNotNull(lock);
                ConfigurationNode node = null;

                lock.lock();
                try {
                    Path path = this.destinationDirectory.resolve(position.getKey() + ".json");
                    if (Files.exists(path)) {
                        node = this.loader.loader(path).load();
                    }

                    if (node == null) {
                        return Boolean.FALSE;
                    }

                    String worldName = node.node("world").getString();

                    if (worldName == null) {
                        return Boolean.FALSE;
                    }

                    W world = this.worldObtainer.getByName(worldName);

                    if (world == null) {
                        return Boolean.FALSE;
                    }

                    double x = node.node("x").getDouble();
                    double y = node.node("y").getDouble();
                    double z = node.node("z").getDouble();
                    float yaw = node.node("yaw").getFloat();
                    float pitch = node.node("pitch").getFloat();

                    position.configure(x,y,z,world,yaw,pitch);
                } finally {
                    lock.unlock();
                }
            } catch (Exception e) {
                throw new RuntimeFileIOException(e);
            }
            return Boolean.TRUE;
        });
    }

    @Override
    public CompletableFuture<Boolean> saveData(Position<E,W> position) {
        return this.taskCoordinator.supply(() -> {
            try {
                Path path = this.destinationDirectory.resolve(position.getKey() + ".json");
                Lock lock = this.ioLocks.get(position);
                Preconditions.checkNotNull(lock);

                lock.lock();
                try {
                    if (!position.hasLocation()) {
                        Files.deleteIfExists(path);
                        return Boolean.FALSE;
                    }

                    ConfigurationNode node = BasicConfigurationNode.root();
                    W world = position.getWorld();

                    if (world == null) {
                        return Boolean.FALSE;
                    }

                    node.node("world").set(position.getWorldName());
                    node.node("x").set(position.getX());
                    node.node("y").set(position.getY());
                    node.node("z").set(position.getZ());
                    node.node("yaw").set(position.getYaw());
                    node.node("pitch").set(position.getPitch());

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
