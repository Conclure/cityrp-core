package me.conclure.cityrp.common.model.character;

import me.conclure.cityrp.common.model.user.User;
import me.conclure.cityrp.common.sender.PlayerSender;
import net.kyori.adventure.text.Component;

import java.util.UUID;

public interface Character<SS> extends PlayerSender<SS> {
    User<SS> getOwner();

    UUID getUniqueId();

    String getName();

    default Component getNameComponent() {
        return Component.text(this.getName());
    }

    void setName(String name);
}
