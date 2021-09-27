package me.conclure.cityrp.common.sender;

import me.conclure.cityrp.common.utility.Delegable;
import net.kyori.adventure.text.Component;

public interface ActionBarSender<SS> extends Delegable<SS> {
    void sendActionBar(Component component);
}
