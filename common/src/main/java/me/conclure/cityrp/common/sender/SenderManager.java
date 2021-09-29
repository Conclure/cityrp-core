package me.conclure.cityrp.common.sender;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;

public interface SenderManager<PlatformSender> {

    void message(Sender<? extends PlatformSender> sender, Component component);

    void title(TitleSender<? extends PlatformSender> sender, Title title);

    void actionBar(ActionBarSender<? extends PlatformSender> sender, Component component);

    void showBossBar(BossBarSender<? extends PlatformSender> sender, BossBar bossBar);

    void hideBossBar(BossBarSender<? extends PlatformSender> sender, BossBar bossBar);

    void openBook(BookSender<? extends PlatformSender> sender, Book book);

    void openBook(BookSender<? extends PlatformSender> sender, Book.Builder bookBuilder);
}
