package me.conclure.cityrp.common.model.user;

import com.google.common.collect.UnmodifiableIterator;
import org.jetbrains.annotations.NotNull;
import org.jspecify.nullness.Nullable;

import java.util.UUID;

public interface UserRepository extends Iterable<User> {

    @Nullable
    User getIfPresent(UUID uniqueId);

    User getOrCreate(UUID uniqueId);

    default boolean contains(UUID uniqueId) {
        return this.getIfPresent(uniqueId) != null;
    }

    default boolean contains(User user) {
        return this.contains(user.getUniqueId());
    }

    User discard(UUID uniqueId);

    default boolean discard(User user) {
        return this.discard(user.getUniqueId()) != null;
    }

    @NotNull
    @Override
    UnmodifiableIterator<User> iterator();
}
