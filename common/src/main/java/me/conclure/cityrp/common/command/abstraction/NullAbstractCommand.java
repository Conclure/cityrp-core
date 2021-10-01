package me.conclure.cityrp.common.command.abstraction;

import me.conclure.cityrp.common.command.CommandInfo;
import me.conclure.cityrp.common.sender.Sender;

public abstract class NullAbstractCommand<S extends Sender>
        extends AbstractCommand<S> {
    protected NullAbstractCommand(CommandInfo<S> commandInfo) {
        super(commandInfo);
    }

    @Override
    public void run(Sender sender, String[] arguments) {
        AbstractCommand.run0(this, sender, arguments, this::execute);
    }

    protected abstract void execute(S sender) throws Exception;
}
