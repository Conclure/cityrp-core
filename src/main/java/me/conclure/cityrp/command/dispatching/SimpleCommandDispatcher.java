package me.conclure.cityrp.command.dispatching;

import com.google.common.base.Preconditions;
import me.conclure.cityrp.command.Command;
import me.conclure.cityrp.command.CommandException;
import me.conclure.cityrp.sender.Sender;
import me.conclure.cityrp.utility.logging.Logger;

public class SimpleCommandDispatcher<SS> implements CommandDispatcher<SS> {
    private final Logger logger;

    public SimpleCommandDispatcher(Logger logger) {
        Preconditions.checkNotNull(logger);
        this.logger = logger;
    }

    @Override
    public void dispatch(Command<? extends Sender<SS>,SS> command, Sender<SS> sender, String[] args) {
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
