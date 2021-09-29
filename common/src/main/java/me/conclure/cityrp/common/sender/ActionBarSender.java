package me.conclure.cityrp.common.sender;

import me.conclure.cityrp.common.utility.Delegable;
import net.kyori.adventure.text.Component;

public interface ActionBarSender<PlatformSender> extends Delegable<PlatformSender> {
    void sendActionBar(Component component);
}
