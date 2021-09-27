package me.conclure.cityrp.sender;

import me.conclure.cityrp.utility.Delegable;
import net.kyori.adventure.inventory.Book;

public interface BookSender<SS> extends Delegable<SS> {
    void openBook(Book book);

    void openBook(Book.Builder bookBuilder);
}
