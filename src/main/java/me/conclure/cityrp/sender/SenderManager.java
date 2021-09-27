package me.conclure.cityrp.sender;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;

public interface SenderManager<SS> {

    void message(Sender<? extends SS> sender, Component component);

    void title(TitleSender<? extends SS> sender, Title title);

    void actionBar(ActionBarSender<? extends SS> sender, Component component);

    void showBossBar(BossBarSender<? extends SS> sender, BossBar bossBar);

    void hideBossBar(BossBarSender<? extends SS> sender, BossBar bossBar);

    void openBook(BookSender<? extends SS> sender, Book book);

    void openBook(BookSender<? extends SS> sender, Book.Builder bookBuilder);

    Sender<? extends SS> asSender(SS sender);

    @FunctionalInterface
    interface Transformer<SS,R extends Sender<SS>> {
        R transform(SS sender);
    }
}
