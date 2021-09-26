package me.conclure.cityrp.sender;

import me.conclure.cityrp.utility.Delegable;
import net.kyori.adventure.text.Component;

public interface ActionBarSender<SS> extends Delegable<SS> {
    void sendActionBar(Component component);
}
