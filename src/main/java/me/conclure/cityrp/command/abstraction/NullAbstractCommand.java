package me.conclure.cityrp.command.abstraction;

import me.conclure.cityrp.command.CommandInfo;
import me.conclure.cityrp.sender.Sender;
import org.bukkit.command.CommandSender;

public abstract class NullAbstractCommand<S extends Sender<SS>,SS> extends AbstractCommand<S,SS> {
    protected NullAbstractCommand(CommandInfo<S,SS> commandInfo) {
        super(commandInfo);
    }

    @Override
    public void run(Sender<SS> sender, String[] arguments) {
        AbstractCommand.run0(this, sender, arguments, this::execute);
    }

    protected abstract void execute(S sender) throws Exception;
}
