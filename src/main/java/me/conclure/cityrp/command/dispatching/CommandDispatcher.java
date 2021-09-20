package me.conclure.cityrp.command.dispatching;

import me.conclure.cityrp.command.Command;
import org.bukkit.command.CommandSender;

public interface CommandDispatcher {

    <S extends CommandSender> void dispatch(Command<S> command, CommandSender sender, String[] args);
}
