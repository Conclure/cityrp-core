package me.conclure.cityrp.common.command.dispatching;

import me.conclure.cityrp.common.command.Command;
import me.conclure.cityrp.common.sender.Sender;

public interface CommandDispatcher<SS> {

    void dispatch(Command<? extends Sender<SS>,SS> command, Sender<? extends SS> sender, String[] args);
}
