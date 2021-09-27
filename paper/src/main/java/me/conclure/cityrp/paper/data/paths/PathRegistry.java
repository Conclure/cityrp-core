package me.conclure.cityrp.paper.data.paths;

import me.conclure.cityrp.common.utility.Key;
import org.jspecify.nullness.Nullable;

import java.nio.file.Path;

public interface PathRegistry extends Iterable<Path> {
    @Nullable
    Path get(Key key);
}
