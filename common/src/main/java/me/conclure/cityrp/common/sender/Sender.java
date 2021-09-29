package me.conclure.cityrp.common.sender;

import me.conclure.cityrp.common.utility.Delegable;
import net.kyori.adventure.text.Component;

public interface Sender<PlatformSender> extends Delegable<PlatformSender> {
    void sendMessage(Component component);

    boolean hasPermission(String permission);
}
