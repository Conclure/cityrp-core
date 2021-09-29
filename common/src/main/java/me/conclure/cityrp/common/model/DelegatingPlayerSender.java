package me.conclure.cityrp.common.model;

import me.conclure.cityrp.common.sender.PlayerSender;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;

public abstract class DelegatingPlayerSender<S extends PlayerSender<? extends PlatformPlayer>, PlatformPlayer>
        implements PlayerSender<PlatformPlayer> {
    protected abstract S getDelegate();

    @Override
    public void sendActionBar(Component component) {
        this.getDelegate().sendActionBar(component);
    }

    @Override
    public void openBook(Book book) {
        this.getDelegate().openBook(book);
    }

    @Override
    public void openBook(Book.Builder bookBuilder) {
        this.getDelegate().openBook(bookBuilder);
    }

    @Override
    public void showBossBar(BossBar bossBar) {
        this.getDelegate().showBossBar(bossBar);
    }

    @Override
    public void hideBossBar(BossBar bossBar) {
        this.getDelegate().hideBossBar(bossBar);
    }

    @Override
    public void sendMessage(Component component) {
        this.getDelegate().sendMessage(component);
    }

    @Override
    public boolean hasPermission(String permission) {
        return this.getDelegate().hasPermission(permission);
    }

    @Override
    public void sendTitle(Title title) {
        this.getDelegate().sendTitle(title);
    }

    @Override
    public PlatformPlayer delegate() {
        return this.getDelegate().delegate();
    }
}
