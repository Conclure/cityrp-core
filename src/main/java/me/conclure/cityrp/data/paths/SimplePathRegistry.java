package me.conclure.cityrp.data.paths;

import com.google.common.base.Preconditions;
import me.conclure.cityrp.utility.Key;
import org.jetbrains.annotations.NotNull;
import org.jspecify.nullness.Nullable;

import java.nio.file.Path;
import java.util.Iterator;
import java.util.Map;

public class SimplePathRegistry implements PathRegistry {
    private final Map<Key, Path> map;

    public SimplePathRegistry(Map<Key, Path> map) {
        Preconditions.checkNotNull(map);

        this.map = map;
    }

    @Override
    public void set(Key key, Path path) {
        Preconditions.checkNotNull(key);

        this.map.put(key, path);
    }

    @Override
    @Nullable
    public Path get(Key key) {
        Preconditions.checkNotNull(key);

        return this.map.get(key);
    }

    @Override
    public @NotNull Iterator<Path> iterator() {
        return this.map.values().iterator();
    }
}
