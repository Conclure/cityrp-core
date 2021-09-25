package me.conclure.cityrp.sender.impl;

import me.conclure.cityrp.sender.ActionBarSender;
import me.conclure.cityrp.sender.Sender;
import me.conclure.cityrp.sender.SenderManager;
import me.conclure.cityrp.sender.SenderMappingRegistry;
import me.conclure.cityrp.sender.TitleSender;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BukkitSenderManager implements SenderManager<CommandSender> {
    private final BukkitAudiences bukkitAudiences;
    private final SenderMappingRegistry<CommandSender> senderMappingRegistry;

    public BukkitSenderManager(BukkitAudiences bukkitAudiences, SenderMappingRegistry<CommandSender> senderMappingRegistry) {
        this.bukkitAudiences = bukkitAudiences;
        this.senderMappingRegistry = senderMappingRegistry;
        senderMappingRegistry.add(Player.class,player -> new BukkitPlayerSender<>(player,this));
    }

    @Override
    public SenderMappingRegistry<CommandSender> getRegistry() {
        return this.senderMappingRegistry;
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
    public Sender<CommandSender> asSender(CommandSender sender) {
        Sender<CommandSender> commandSender = this.senderMappingRegistry.get(sender.getClass(), sender);
        if (commandSender == null) {
            return new BukkitSender<>(sender);
        }
        return commandSender;
    }

    @Override
    public <R extends Sender<CommandSender>> R asSender(CommandSender sender, Transformer<CommandSender,R> transformer) {
        return transformer.transform(sender);
    }
}
