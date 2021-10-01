package me.conclure.cityrp.common.command;

import me.conclure.cityrp.common.sender.Sender;

public interface Command<S extends Sender> {
    boolean acceptsSender(Sender sender);

    boolean acceptsSenderType(Class<? extends Sender> type);

    boolean hasPermission(Sender sender);

    CommandInfo<S> getInfo();

    void run(Sender sender, String[] arguments);

}
