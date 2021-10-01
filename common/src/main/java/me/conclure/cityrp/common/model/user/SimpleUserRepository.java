package me.conclure.cityrp.common.model.user;

import org.jspecify.nullness.Nullable;

import java.util.Map;
import java.util.UUID;

public class SimpleUserRepository implements UserRepository {
    private final Map<UUID,User> map;
    private final UserFactory userFactory;

    public SimpleUserRepository(Map<UUID, User> map, UserFactory userFactory) {
        this.map = map;
        this.userFactory = userFactory;
    }

    @Override
    public @Nullable User getIfPresent(UUID uniqueId) {
        return this.map.get(uniqueId);
    }

    @Override
    public User getOrCreate(UUID uniqueId) {
        return this.map.computeIfAbsent(uniqueId,this.userFactory::create);
    }

    @Override
    public User unload(UUID uniqueId) {
        return this.map.remove(uniqueId);
    }
}
