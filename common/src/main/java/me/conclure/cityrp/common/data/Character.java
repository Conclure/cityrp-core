package me.conclure.cityrp.common.data;

import net.kyori.adventure.text.Component;

import java.util.UUID;

public interface Character {
    UUID getUniqueId();

    String getName();

    default Component getNameComponent() {
        return Component.text(this.getName());
    }

    boolean rename(String name);
}
