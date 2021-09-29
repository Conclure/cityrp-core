package me.conclure.cityrp.common.data.character;

import me.conclure.cityrp.common.model.character.Character;
import me.conclure.cityrp.common.model.user.User;

import java.util.concurrent.CompletableFuture;

public interface CharacterDataManager<PlatformPlayer> {
    CompletableFuture<Void> load(Character<PlatformPlayer> character);

    CompletableFuture<Void> save(Character<PlatformPlayer> character);
}
