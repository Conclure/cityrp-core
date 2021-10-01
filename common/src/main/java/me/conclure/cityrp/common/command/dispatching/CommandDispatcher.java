package me.conclure.cityrp.common.command.dispatching;

import me.conclure.cityrp.common.command.Command;
import me.conclure.cityrp.common.sender.Sender;

public interface CommandDispatcher {

    void dispatch(
            Command<? extends Sender> command,
            Sender sender,
            String[] args
    );
}
