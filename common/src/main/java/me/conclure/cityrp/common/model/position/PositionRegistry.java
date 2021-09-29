package me.conclure.cityrp.common.model.position;

import me.conclure.cityrp.common.utility.Key;
import org.jspecify.nullness.Nullable;

import java.util.OptionalInt;
import java.util.stream.Stream;

public interface PositionRegistry<PlatformEntity, PlatformWorld> {

    @Nullable
    Position<PlatformEntity, PlatformWorld> getByKey(Key key);

    Stream<Position<PlatformEntity, PlatformWorld>> stream();

    int size();

    OptionalInt getId(Key key);

    default OptionalInt getId(Position<PlatformEntity, PlatformWorld> position) {
        return this.getId(position.getKey());
    }

    @Nullable
    Position<PlatformEntity, PlatformWorld> getById(int id);
}
