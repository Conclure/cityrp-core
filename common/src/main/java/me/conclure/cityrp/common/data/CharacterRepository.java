package me.conclure.cityrp.common.data;

import org.jspecify.nullness.Nullable;

import java.util.UUID;

public interface CharacterRepository {
    @Nullable
    Character getIfPresent(User user, UUID uniqueId);

    Character getOrCreate(User user, UUID uniqueId);

    default boolean contains(User user, UUID uniqueId) {
        return this.getIfPresent(user,uniqueId) != null;
    }
}
