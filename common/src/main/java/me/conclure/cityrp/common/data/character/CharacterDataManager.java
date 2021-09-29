package me.conclure.cityrp.common.data.character;

import me.conclure.cityrp.common.model.character.Character;
import me.conclure.cityrp.common.model.user.User;

import java.util.concurrent.CompletableFuture;

public interface CharacterDataManager<SS> {
    CompletableFuture<Void> load(Character<SS> character);

    CompletableFuture<Void> save(Character<SS> character);
}
