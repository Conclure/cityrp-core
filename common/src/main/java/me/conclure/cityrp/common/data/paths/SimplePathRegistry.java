package me.conclure.cityrp.common.data.paths;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import me.conclure.cityrp.common.utility.Key;
import org.jetbrains.annotations.NotNull;
import org.jspecify.nullness.Nullable;

import java.nio.file.Path;
import java.util.Iterator;

public class SimplePathRegistry implements PathRegistry {
    private final ImmutableMap<Key, Path> map;

    public SimplePathRegistry(ImmutableMap<Key, Path> map) {
        Preconditions.checkNotNull(map);

        this.map = map;
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

    public static class Builder {
        private ImmutableMap.Builder<Key,Path> map;

        public Builder add(Key key, Path path) {
            map.put(key,path);
            return this;
        }

        public SimplePathRegistry build() {
            return new SimplePathRegistry(this.map.build());
        }
    }
}
