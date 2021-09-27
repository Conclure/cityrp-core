package me.conclure.cityrp.paper.sender;

import me.conclure.cityrp.common.sender.PlayerSender;
import me.conclure.cityrp.common.sender.SenderManager;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BukkitPlayerSender<SS extends Player> extends BukkitSender<SS> implements PlayerSender<SS> {
    private final SenderManager<CommandSender> senderManager;

    BukkitPlayerSender(SS player, SenderManager<CommandSender> senderManager) {
        super(player);
        this.senderManager = senderManager;
    }

    @Override
    public void sendMessage(Component component) {
        this.senderManager.message(this,component);
    }

    @Override
    public void sendActionBar(Component component) {
        this.senderManager.actionBar(this,component);
    }

    @Override
    public void sendTitle(Title title) {
        this.senderManager.title(this,title);
    }

    @Override
    public void showBossBar(BossBar bossBar) {
        this.senderManager.showBossBar(this,bossBar);
    }

    @Override
    public void hideBossBar(BossBar bossBar) {
        this.senderManager.hideBossBar(this,bossBar);
    }

    @Override
    public void openBook(Book book) {
        this.senderManager.openBook(this,book);
    }

    @Override
    public void openBook(Book.Builder bookBuilder) {
        this.senderManager.openBook(this,bookBuilder);
    }
}
