package me.conclure.cityrp.common.position;

import me.conclure.cityrp.common.utility.Key;
import org.jspecify.nullness.Nullable;

import java.util.OptionalInt;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public interface PositionRegistry<E,W> {

    @Nullable
    Position<E,W> getByKey(Key key);

    Stream<Position<E,W>> stream();

    int size();

    OptionalInt getId(Key key);

    default OptionalInt getId(Position<E,W> position) {
        return this.getId(position.getKey());
    }

    @Nullable
    Position<E,W> getById(int id);
}
