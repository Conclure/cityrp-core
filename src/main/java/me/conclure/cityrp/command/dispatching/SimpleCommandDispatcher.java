package me.conclure.cityrp.command.dispatching;

import com.google.common.base.Preconditions;
import me.conclure.cityrp.command.Command;
import me.conclure.cityrp.command.CommandException;
import me.conclure.cityrp.utility.logging.Logger;
import org.bukkit.command.CommandSender;

public class SimpleCommandDispatcher implements CommandDispatcher {
    private final Logger logger;

    public SimpleCommandDispatcher(Logger logger) {
        Preconditions.checkNotNull(logger);
        this.logger = logger;
    }

    @Override
    public <S extends CommandSender> void dispatch(Command<S> command, CommandSender sender, String[] args) {
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
