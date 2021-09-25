package me.conclure.cityrp.command;

import me.conclure.cityrp.sender.Sender;
import org.bukkit.command.CommandSender;

public interface Command<S extends Sender<SS>,SS> {
    boolean acceptsSender(Sender<SS> sender);

    boolean acceptsSenderType(Class<? extends Sender<SS>> type);

    boolean hasPermission(Sender<SS> sender);

    CommandInfo<S,SS> getInfo();

    void run(Sender<SS> sender, String[] arguments);

}
