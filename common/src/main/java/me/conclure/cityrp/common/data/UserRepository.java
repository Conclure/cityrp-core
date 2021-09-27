package me.conclure.cityrp.common.data;

import org.jspecify.nullness.Nullable;

import java.util.UUID;

public interface UserRepository {

    @Nullable
    Character getIfPresent(UUID uniqueId);

    Character getOrCreate(UUID uniqueId);

    default boolean contains(UUID uniqueId) {
        return this.getIfPresent(uniqueId) != null;
    }
}
