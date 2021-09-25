package me.conclure.cityrp.sender;

import me.conclure.cityrp.utility.Delegeteable;
import net.kyori.adventure.text.Component;

public interface ActionBarSender<SS> extends Delegeteable<SS> {
    void sendActionBar(Component component);
}
