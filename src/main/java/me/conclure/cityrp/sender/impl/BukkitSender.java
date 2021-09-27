package me.conclure.cityrp.sender.impl;

import me.conclure.cityrp.sender.Sender;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;

public class BukkitSender<SS extends CommandSender> implements Sender<SS> {
    private final SS sender;

    BukkitSender(SS sender) {
        this.sender = sender;
    }

    @Override
    public void sendMessage(Component component) {
        this.sender.sendMessage(component);
    }

    @Override
    public SS delegate() {
        return this.sender;
    }

    @Override
    public boolean hasPermission(String permission) {
        return this.sender.hasPermission(permission);
    }
}
