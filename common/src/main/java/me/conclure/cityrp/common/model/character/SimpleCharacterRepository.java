package me.conclure.cityrp.common.model.character;

import org.jspecify.nullness.Nullable;

import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

public class SimpleCharacterRepository implements CharacterRepository {
    private final Map<UUID, Map<UUID,Character>> characterCache;
    private final Supplier<? extends Map<UUID,Character>> mapFactory;

    public SimpleCharacterRepository(
            Map<UUID, Map<UUID, Character>> characterCache,
            Supplier<? extends Map<UUID, Character>> mapFactory
    ) {
        this.characterCache = characterCache;
        this.mapFactory = mapFactory;
    }

    @Override
    public @Nullable Character getIfPresent(UUID userUniqueId, UUID uniqueId) {
        return null;
    }

    @Override
    public Character getOrCreate(UUID userUniqueId, UUID uniqueId) {
        return null;
    }
}
