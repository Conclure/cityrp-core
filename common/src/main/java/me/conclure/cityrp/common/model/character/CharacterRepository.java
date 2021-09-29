package me.conclure.cityrp.common.model.character;

import me.conclure.cityrp.common.model.user.User;
import org.jspecify.nullness.Nullable;

import java.util.UUID;

public interface CharacterRepository<SS> {
    @Nullable
    Character<SS> getIfPresent(UUID userUniqueId, UUID uniqueId);

    Character<SS> getOrCreate(UUID userUniqueId, UUID uniqueId);

    default boolean contains(UUID userUniqueId, UUID uniqueId) {
        return this.getIfPresent(userUniqueId,uniqueId) != null;
    }
}
