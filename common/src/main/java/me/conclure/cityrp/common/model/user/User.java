package me.conclure.cityrp.common.model.user;

import me.conclure.cityrp.common.model.character.Character;
import me.conclure.cityrp.common.sender.PlayerSender;
import net.kyori.adventure.text.Component;

import java.util.List;
import java.util.UUID;

public interface User extends PlayerSender {
    String getName();

    void setName(String name);

    default Component getNameComponent() {
        return Component.text(this.getName());
    }

    UUID getUniqueId();

    int getCharacterAmount();

    void setCharacterAmount(int characterAmount);

    int getMaxCharacterAmount();

    void setMaxCharacterAmount(int maxCharacterAmount);
}
