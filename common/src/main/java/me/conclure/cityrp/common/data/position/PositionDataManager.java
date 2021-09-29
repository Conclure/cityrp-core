package me.conclure.cityrp.common.data.position;

import me.conclure.cityrp.common.model.position.Position;

import java.util.concurrent.CompletableFuture;

public interface PositionDataManager<PlatformEntity, PlatformWorld> {

    CompletableFuture<Void> loadAll();

    CompletableFuture<Void> saveAll();

    CompletableFuture<Boolean> load(Position<PlatformEntity, PlatformWorld> position);

    CompletableFuture<Boolean> save(Position<PlatformEntity, PlatformWorld> position);
}
