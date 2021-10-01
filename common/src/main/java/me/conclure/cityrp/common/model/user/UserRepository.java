package me.conclure.cityrp.common.model.user;

import org.jspecify.nullness.Nullable;

import java.util.UUID;

public interface UserRepository {

    @Nullable
    User getIfPresent(UUID uniqueId);

    User getOrCreate(UUID uniqueId);

    default boolean contains(UUID uniqueId) {
        return this.getIfPresent(uniqueId) != null;
    }

    default boolean contains(User user) {
        return this.contains(user.getUniqueId());
    }

    User unload(UUID uniqueId);

    default boolean unload(User user) {
        return this.unload(user.getUniqueId()) != null;
    }
}
