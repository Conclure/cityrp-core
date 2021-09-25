package me.conclure.cityrp.data;

import me.conclure.cityrp.utility.collections.LoadingMap;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class UserCache {
    private final LoadingMap<UUID, User> cache;

    public UserCache(Function<UUID, User> loadingFunction) {
        this.cache = new LoadingMap<>(new ConcurrentHashMap<>(), loadingFunction);
    }


}
