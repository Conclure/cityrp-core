package me.conclure.cityrp.common.model.user;

import org.jspecify.nullness.Nullable;

import java.util.UUID;

public interface UserRepository<PlatformPlayer> {

    @Nullable
    User<PlatformPlayer> getIfPresent(UUID uniqueId);

    User<PlatformPlayer> getOrCreate(UUID uniqueId);

    default boolean contains(UUID uniqueId) {
        return this.getIfPresent(uniqueId) != null;
    }
}
