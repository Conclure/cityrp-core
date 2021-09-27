package me.conclure.cityrp.common.data;

import java.util.concurrent.CompletableFuture;

public interface CharacterDataManager {
    CompletableFuture<Void> load(Character character);

    CompletableFuture<Void> save(Character character);

    CompletableFuture<Void> saveAll(User user);

    CompletableFuture<Void> loadAll(User user);
}
