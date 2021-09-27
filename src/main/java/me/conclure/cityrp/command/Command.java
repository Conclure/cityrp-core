package me.conclure.cityrp.command;

import me.conclure.cityrp.sender.Sender;
import org.bukkit.command.CommandSender;

public interface Command<S extends Sender<SS>,SS> {
    boolean acceptsSender(Sender<? extends SS> sender);

    boolean acceptsSenderType(Class<? extends Sender<? extends SS>> type);

    boolean hasPermission(Sender<? extends SS> sender);

    CommandInfo<S,SS> getInfo();

    void run(Sender<? extends SS> sender, String[] arguments);

}
