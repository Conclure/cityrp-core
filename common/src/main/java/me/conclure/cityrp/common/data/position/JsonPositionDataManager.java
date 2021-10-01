package me.conclure.cityrp.common.data.position;

import com.github.benmanes.caffeine.cache.LoadingCache;
import com.google.common.base.Preconditions;
import me.conclure.cityrp.common.model.position.Position;
import me.conclure.cityrp.common.model.position.PositionRegistry;
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
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;

public class JsonPositionDataManager<PlatformEntity, PlatformWorld>
        implements PositionDataManager<PlatformEntity, PlatformWorld> {
    private final PositionRegistry<PlatformEntity, PlatformWorld> positionRegistry;
    private final WorldObtainer<PlatformWorld> worldObtainer;
    private final Path destinationDirectory;
    private final ConfigurateLoaderFactory loader;
    private final LoadingCache<Position<PlatformEntity, PlatformWorld>, ReadWriteLock> ioLocks;
    private final TaskCoordinator<? extends Executor> taskCoordinator;

    public JsonPositionDataManager(
            PositionRegistry<PlatformEntity, PlatformWorld> positionRegistry,
            WorldObtainer<PlatformWorld> worldObtainer,
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
                .build(key -> new ReentrantReadWriteLock(true));
    }

    private CompletableFuture<Void> applyAllPositionsToFuture(Function<Position<PlatformEntity, PlatformWorld>, CompletableFuture<?>> function) {
        CompletableFuture<?>[] futures = this.positionRegistry.stream()
                .map(function)
                .toArray(CompletableFuture[]::new);
        return CompletableFuture.allOf(futures);
    }

    @Override
    public CompletableFuture<Void> loadAll() {
        return this.applyAllPositionsToFuture(this::load);
    }

    @Override
    public CompletableFuture<Void> saveAll() {
        return this.applyAllPositionsToFuture(this::save);
    }

    @Override
    public CompletableFuture<Boolean> load(Position<PlatformEntity, PlatformWorld> position) {
        return this.taskCoordinator.supply(() -> {
            try {
                ReadWriteLock lock = this.ioLocks.get(position);
                Preconditions.checkNotNull(lock);
                ConfigurationNode node = null;

                lock.readLock().lock();
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

                    PlatformWorld world = this.worldObtainer.getByName(worldName);

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
                    lock.readLock().unlock();
                }
            } catch (Exception e) {
                throw new RuntimeFileIOException(e);
            }
            return Boolean.TRUE;
        });
    }

    @Override
    public CompletableFuture<Boolean> save(Position<PlatformEntity, PlatformWorld> position) {
        return this.taskCoordinator.supply(() -> {
            try {
                Path path = this.destinationDirectory.resolve(position.getKey() + ".json");
                ReadWriteLock lock = this.ioLocks.get(position);
                Preconditions.checkNotNull(lock);

                lock.writeLock().lock();
                try {
                    if (!position.hasLocation()) {
                        Files.deleteIfExists(path);
                        return Boolean.FALSE;
                    }

                    ConfigurationNode node = BasicConfigurationNode.root();
                    PlatformWorld world = position.getWorld();

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
                    lock.writeLock().unlock();
                }
            } catch (Exception e) {
                throw new RuntimeFileIOException(e);
            }

            return Boolean.TRUE;
        });
    }
}
