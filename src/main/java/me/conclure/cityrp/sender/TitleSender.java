package me.conclure.cityrp.sender;

import me.conclure.cityrp.utility.Delegeteable;
import net.kyori.adventure.title.Title;

public interface TitleSender<SS> extends Delegeteable<SS> {
    void sendTitle(Title title);
}
