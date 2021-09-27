package me.conclure.cityrp.sender;

import me.conclure.cityrp.utility.Delegable;
import net.kyori.adventure.title.Title;

public interface TitleSender<SS> extends Delegable<SS> {
    void sendTitle(Title title);
}
