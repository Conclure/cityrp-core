package me.conclure.cityrp.common.model.user;

import me.conclure.cityrp.common.model.character.Character;
import me.conclure.cityrp.common.sender.PlayerSender;
import net.kyori.adventure.text.Component;

import java.util.List;
import java.util.UUID;

public interface User<PlatformPlayer> extends PlayerSender<PlatformPlayer> {
    String getName();

    default Component getNameComponent() {
        return Component.text(this.getName());
    }

    UUID getUniqueId();

    List<Character<PlatformPlayer>> getCharacters();

    int getCharacterAmount();

    int getMaxCharacterAmount();
}
