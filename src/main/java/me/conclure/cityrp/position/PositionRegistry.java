package me.conclure.cityrp.position;

import me.conclure.cityrp.utility.Key;
import org.jspecify.nullness.Nullable;

import java.util.function.Consumer;

public interface PositionRegistry {
    @Nullable
    Position get(Key key);

    void forEach(Consumer<? super Position> consumer);
}
