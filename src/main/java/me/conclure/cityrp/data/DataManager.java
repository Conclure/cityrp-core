package me.conclure.cityrp.data;

import java.util.concurrent.CompletableFuture;

public interface DataManager {
    CompletableFuture<Void> loadAll();

    CompletableFuture<Void> saveAll();
}
