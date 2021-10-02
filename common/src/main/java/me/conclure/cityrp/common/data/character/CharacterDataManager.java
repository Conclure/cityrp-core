package me.conclure.cityrp.common.data.character;

import me.conclure.cityrp.common.model.character.Character;

import java.util.concurrent.CompletableFuture;

public interface CharacterDataManager {
    CompletableFuture<Void> load(Character character);

    CompletableFuture<Void> save(Character character);
}
