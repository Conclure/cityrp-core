package me.conclure.cityrp.sender;

import me.conclure.cityrp.utility.Delegeteable;
import net.kyori.adventure.text.Component;

public interface Sender<SS> extends Delegeteable<SS> {
    void sendMessage(Component component);

    boolean hasPermission(String permission);
}
