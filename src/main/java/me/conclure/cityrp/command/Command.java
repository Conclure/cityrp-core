package me.conclure.cityrp.command;

import org.bukkit.command.CommandSender;

public interface Command<S extends CommandSender> {
    boolean acceptsSender(CommandSender sender);

    boolean acceptsSenderType(Class<? extends CommandSender> type);

    boolean hasPermission(CommandSender sender);

    CommandInfo<S> getInfo();

    void run(CommandSender sender, String[] arguments);

}
