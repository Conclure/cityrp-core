package me.conclure.cityrp.common.data;

import net.kyori.adventure.text.Component;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface User {
    String getName();

    default Component getNameComponent() {
        return Component.text(this.getName());
    }

    UUID getUniqueId();

    List<Character> getCharacters();

    int getCharacterAmount();

    int getMaxCharacterAmount();
}
