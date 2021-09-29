package me.conclure.cityrp.common.command.dispatching;

import me.conclure.cityrp.common.command.Command;
import me.conclure.cityrp.common.sender.Sender;

public interface CommandDispatcher<PlatformSender> {

    void dispatch(
            Command<? extends Sender<PlatformSender>, PlatformSender> command,
            Sender<? extends PlatformSender> sender,
            String[] args
    );
}
