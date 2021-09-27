package me.conclure.cityrp.paper.data;

import java.util.concurrent.CompletableFuture;

public interface DataManager {
    CompletableFuture<Void> loadAll();

    CompletableFuture<Void> saveAll();
}
