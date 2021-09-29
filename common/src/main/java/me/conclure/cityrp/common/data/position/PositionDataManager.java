package me.conclure.cityrp.common.data.position;

import me.conclure.cityrp.common.model.position.Position;

import java.util.concurrent.CompletableFuture;

public interface PositionDataManager<E,W> {

    CompletableFuture<Void> loadAll();

    CompletableFuture<Void> saveAll();

    CompletableFuture<Boolean> load(Position<E,W> position);

    CompletableFuture<Boolean> save(Position<E,W> position);
}
