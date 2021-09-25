package me.conclure.cityrp.sender.impl;

import me.conclure.cityrp.sender.PlayerSender;
import me.conclure.cityrp.sender.SenderManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BukkitPlayerSender<SS extends Player> extends BukkitSender<SS> implements PlayerSender<SS> {
    private final SenderManager<CommandSender> senderManager;

    public BukkitPlayerSender(SS player, SenderManager<CommandSender> senderManager) {
        super(player);
        this.senderManager = senderManager;
    }

    @Override
    public void sendActionBar(Component component) {
        this.senderManager.actionBar(this,component);
    }

    @Override
    public void sendTitle(Title title) {
        this.senderManager.title(this,title);
    }
}
