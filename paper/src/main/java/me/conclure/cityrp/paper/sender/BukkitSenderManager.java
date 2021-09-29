package me.conclure.cityrp.paper.sender;

import me.conclure.cityrp.common.sender.ActionBarSender;
import me.conclure.cityrp.common.sender.BookSender;
import me.conclure.cityrp.common.sender.BossBarSender;
import me.conclure.cityrp.common.sender.Sender;
import me.conclure.cityrp.common.sender.SenderManager;
import me.conclure.cityrp.common.sender.TitleSender;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BukkitSenderManager implements SenderManager<CommandSender> {
    private final BukkitAudiences bukkitAudiences;

    public BukkitSenderManager(BukkitAudiences bukkitAudiences) {
        this.bukkitAudiences = bukkitAudiences;
    }

    @Override
    public void message(Sender<? extends CommandSender> sender, Component component) {
        this.bukkitAudiences.sender(sender.delegate()).sendMessage(component);
    }

    @Override
    public void title(TitleSender<? extends CommandSender> sender, Title title) {
        this.bukkitAudiences.sender(sender.delegate()).showTitle(title);
    }

    @Override
    public void actionBar(ActionBarSender<? extends CommandSender> sender, Component component) {
        this.bukkitAudiences.sender(sender.delegate()).sendActionBar(component);
    }

    @Override
    public void showBossBar(BossBarSender<? extends CommandSender> sender, BossBar bossBar) {
        this.bukkitAudiences.sender(sender.delegate()).showBossBar(bossBar);
    }

    @Override
    public void hideBossBar(BossBarSender<? extends CommandSender> sender, BossBar bossBar) {
        this.bukkitAudiences.sender(sender.delegate()).hideBossBar(bossBar);
    }

    @Override
    public void openBook(BookSender<? extends CommandSender> sender, Book book) {
        this.bukkitAudiences.sender(sender.delegate()).openBook(book);
    }

    @Override
    public void openBook(BookSender<? extends CommandSender> sender, Book.Builder bookBuilder) {
        this.bukkitAudiences.sender(sender.delegate()).openBook(bookBuilder);
    }
}
