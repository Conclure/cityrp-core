package me.conclure.cityrp.common.sender;

import me.conclure.cityrp.common.utility.Delegable;
import net.kyori.adventure.title.Title;

public interface TitleSender {
    void showTitle(Title title);

    void clearTitle();
}
