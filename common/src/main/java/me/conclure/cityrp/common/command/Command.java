package me.conclure.cityrp.common.command;

import me.conclure.cityrp.common.sender.Sender;

public interface Command<S extends Sender<SS>,SS> {
    boolean acceptsSender(Sender<? extends SS> sender);

    boolean acceptsSenderType(Class<? extends Sender<? extends SS>> type);

    boolean hasPermission(Sender<? extends SS> sender);

    CommandInfo<S,SS> getInfo();

    void run(Sender<? extends SS> sender, String[] arguments);

}
