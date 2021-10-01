package me.conclure.cityrp.common.command.dispatching;

import com.google.common.base.Preconditions;
import me.conclure.cityrp.common.command.Command;
import me.conclure.cityrp.common.command.CommandException;
import me.conclure.cityrp.common.sender.Sender;
import me.conclure.cityrp.common.utility.logging.Logger;

public class SimpleCommandDispatcher implements CommandDispatcher {
    private final Logger logger;

    public SimpleCommandDispatcher(Logger logger) {
        Preconditions.checkNotNull(logger);
        this.logger = logger;
    }

    @Override
    public void dispatch(
            Command<? extends Sender> command,
            Sender sender,
            String[] args) {
        Preconditions.checkNotNull(command);
        Preconditions.checkNotNull(sender);
        Preconditions.checkNotNull(args);
        try {
            command.run(sender, args);
        } catch (CommandException e) {
            this.logger.error(e);
        }
    }
}
