package me.conclure.cityrp.common.model.user;

import org.jspecify.nullness.Nullable;

import java.util.UUID;

public class SimpleUserRepository<PlatformPlayer> implements UserRepository<PlatformPlayer> {
    @Override
    public @Nullable User<PlatformPlayer> getIfPresent(UUID uniqueId) {
        return null;
    }

    @Override
    public User<PlatformPlayer> getOrCreate(UUID uniqueId) {
        return null;
    }
}
