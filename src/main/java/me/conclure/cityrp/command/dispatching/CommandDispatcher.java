package me.conclure.cityrp.command.dispatching;

import me.conclure.cityrp.command.Command;
import me.conclure.cityrp.sender.Sender;

public interface CommandDispatcher<SS> {

    void dispatch(Command<? extends Sender<SS>,SS> command, Sender<? extends SS> sender, String[] args);
}
