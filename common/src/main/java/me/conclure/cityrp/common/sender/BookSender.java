package me.conclure.cityrp.common.sender;

import me.conclure.cityrp.common.utility.Delegable;
import net.kyori.adventure.inventory.Book;

public interface BookSender<PlatformSender> extends Delegable<PlatformSender> {
    void openBook(Book book);

    void openBook(Book.Builder bookBuilder);
}
