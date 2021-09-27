package me.conclure.cityrp.sender;

import me.conclure.cityrp.utility.Delegable;
import net.kyori.adventure.text.Component;

public interface Sender<SS> extends Delegable<SS> {
    void sendMessage(Component component);

    boolean hasPermission(String permission);
}
