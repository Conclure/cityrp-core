package me.conclure.cityrp.registry;

import org.jspecify.nullness.Nullable;

import java.util.OptionalInt;

public interface Registry<E> extends Iterable<E> {
    OptionalInt getId(E entry);

    @Nullable
    E fromId(int id);

    @Nullable
    E fromKey(Key key);

    Class<E> getBaseType();

    int register(Key key, E entry);
}
