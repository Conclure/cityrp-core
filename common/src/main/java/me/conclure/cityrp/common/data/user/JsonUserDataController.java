package me.conclure.cityrp.common.data.user;

import com.github.benmanes.caffeine.cache.LoadingCache;
import com.google.common.base.Preconditions;
import me.conclure.cityrp.common.model.user.User;
import me.conclure.cityrp.common.utility.CaffeineFactory;
import me.conclure.cityrp.common.utility.ConfigurateLoaderFactory;
import me.conclure.cityrp.common.utility.exception.RuntimeFileIOException;
import org.jspecify.nullness.Nullable;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.jackson.JacksonConfigurationLoader;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class JsonUserDataController implements UserDataController {
    private final ConfigurateLoaderFactory loader;
    private final LoadingCache<Path, ReadWriteLock> ioLocks;
    private final Path destinationDirectory;
    private final ConfigurateUserSerializer configurateUserSerializer;

    public JsonUserDataController(
            Path destinationDirectory,
            ConfigurateUserSerializer configurateUserSerializer
    ) {
        this.destinationDirectory = destinationDirectory;
        this.configurateUserSerializer = configurateUserSerializer;
        this.ioLocks = CaffeineFactory.newBuilder()
                .expireAfterAccess(10, TimeUnit.MINUTES)
                .build(key -> new ReentrantReadWriteLock(true));
        this.loader = path -> JacksonConfigurationLoader.builder()
                .source(() -> Files.newBufferedReader(path, StandardCharsets.UTF_8))
                .sink(() -> Files.newBufferedWriter(path, StandardCharsets.UTF_8))
                .build();
    }

    private Path resolveUserPath(User user) {
        return this.destinationDirectory.resolve(user.getUniqueId() + ".json");
    }

    @Nullable
    private ReadWriteLock getLockForUserPath(Path path) {
        return this.ioLocks.get(path);
    }

    @Override
    public void save(User user) {
        try {
            Path path = this.resolveUserPath(user);
            ReadWriteLock lock = this.getLockForUserPath(path);
            Preconditions.checkNotNull(lock);

            lock.writeLock().lock();
            try {
                ConfigurationNode node = BasicConfigurationNode.root();
                this.configurateUserSerializer.serialize(node, user);

                this.loader.loader(path).save(node);
            } finally {
                lock.writeLock().unlock();
            }
        } catch (Exception e) {
            throw new RuntimeFileIOException(e);
        }
    }

    @Override
    public void load(User user) {
        try {
            Path path = this.resolveUserPath(user);
            ReadWriteLock lock = this.getLockForUserPath(path);
            Preconditions.checkNotNull(lock);

            lock.readLock().lock();
            try {
                ConfigurationNode node = this.loader.loader(path).load();
                this.configurateUserSerializer.serialize(node, user);
            } finally {
                lock.readLock().unlock();
            }
        } catch (Exception e) {
            throw new RuntimeFileIOException(e);
        }
    }
}
