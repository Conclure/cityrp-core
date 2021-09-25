package me.conclure.cityrp.data.paths;

import me.conclure.cityrp.utility.Key;
import org.jspecify.nullness.Nullable;

import java.nio.file.Path;

public interface PathRegistry extends Iterable<Path> {
    void set(Key key, Path path);

    @Nullable Path get(Key key);
}
