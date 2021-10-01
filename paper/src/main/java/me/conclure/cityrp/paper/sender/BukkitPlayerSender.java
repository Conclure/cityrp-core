package me.conclure.cityrp.paper.sender;

import me.conclure.cityrp.common.sender.PlayerSender;
import me.conclure.cityrp.common.sender.SenderTranformer;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;

public class BukkitPlayerSender implements PlayerSender {
    private final BukkitAudiences audiences;
    private final Player player;

    BukkitPlayerSender(Player player, BukkitAudiences audiences) {
        this.player = player;
        this.audiences = audiences;
    }

    @Override
    public void sendMessage(Component component) {
        this.player.sendMessage(component);
    }

    @Override
    public boolean hasPermission(String permission) {
        return this.player.hasPermission(permission);
    }

    @Override
    public void sendActionBar(Component component) {
        this.audiences.player(this.player).sendActionBar(component);
    }

    @Override
    public void showTitle(Title title) {
        this.audiences.player(this.player).showTitle(title);
    }

    @Override
    public void clearTitle() {
        this.audiences.player(this.player).clearTitle();
    }

    @Override
    public void showBossBar(BossBar bossBar) {
        this.audiences.player(this.player).showBossBar(bossBar);
    }

    @Override
    public void hideBossBar(BossBar bossBar) {
        this.audiences.player(this.player).hideBossBar(bossBar);
    }

    @Override
    public void openBook(Book book) {
        this.audiences.player(this.player).openBook(book);
    }

    @Override
    public void openBook(Book.Builder bookBuilder) {
        this.audiences.player(this.player).openBook(bookBuilder);
    }

    public static class Tranformer
            implements SenderTranformer<Player,PlayerSender> {
        private final BukkitAudiences audiences;

        public Tranformer(BukkitAudiences audiences) {
            this.audiences = audiences;
        }

        @Override
        public PlayerSender tranform(Player bukkitPlayer) {
            return new BukkitPlayerSender(bukkitPlayer, this.audiences);
        }
    }
}
