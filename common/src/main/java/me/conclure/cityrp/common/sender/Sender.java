package me.conclure.cityrp.common.sender;

import net.kyori.adventure.text.Component;

public interface Sender {
    void sendMessage(Component component);

    boolean hasPermission(String permission);
}
