package me.conclure.cityrp.common.model;

import me.conclure.cityrp.common.sender.PlayerSender;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;

public abstract class DelegatingPlayerSender
        implements PlayerSender {
    protected abstract PlayerSender delegate();

    @Override
    public void sendActionBar(Component component) {
        this.delegate().sendActionBar(component);
    }

    @Override
    public void openBook(Book book) {
        this.delegate().openBook(book);
    }

    @Override
    public void openBook(Book.Builder bookBuilder) {
        this.delegate().openBook(bookBuilder);
    }

    @Override
    public void showBossBar(BossBar bossBar) {
        this.delegate().showBossBar(bossBar);
    }

    @Override
    public void hideBossBar(BossBar bossBar) {
        this.delegate().hideBossBar(bossBar);
    }

    @Override
    public void sendMessage(Component component) {
        this.delegate().sendMessage(component);
    }

    @Override
    public boolean hasPermission(String permission) {
        return this.delegate().hasPermission(permission);
    }

    @Override
    public void showTitle(Title title) {
        this.delegate().showTitle(title);
    }

    @Override
    public void clearTitle() {
        this.delegate().clearTitle();
    }
}
