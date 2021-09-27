package me.conclure.cityrp.common.position;

import java.util.concurrent.CompletableFuture;

public interface PositionDataManager<E,W> {

    CompletableFuture<Void> loadAll();

    CompletableFuture<Void> saveAll();

    CompletableFuture<Boolean> load(Position<E,W> position);

    CompletableFuture<Boolean> save(Position<E,W> position);
}
