package me.conclure.cityrp.common.model.character;

import org.jspecify.nullness.Nullable;

import java.util.Map;
import java.util.UUID;

public class SimpleCharacterRepository<SS> implements CharacterRepository<SS> {
    private final Map<UUID, Map<UUID,Character<SS>>> characterCache;

    public SimpleCharacterRepository(Map<UUID, Map<UUID, Character<SS>>> characterCache) {
        this.characterCache = characterCache;
    }

    @Override
    public @Nullable Character<SS> getIfPresent(UUID userUniqueId, UUID uniqueId) {
        return null;
    }

    @Override
    public Character<SS> getOrCreate(UUID userUniqueId, UUID uniqueId) {
        return null;
    }
}
