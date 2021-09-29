package me.conclure.cityrp.common.command.abstraction;

import me.conclure.cityrp.common.command.CommandInfo;
import me.conclure.cityrp.common.sender.Sender;

public abstract class NullAbstractCommand<S extends Sender<PlatformSender>, PlatformSender>
        extends AbstractCommand<S, PlatformSender> {
    protected NullAbstractCommand(CommandInfo<S, PlatformSender> commandInfo) {
        super(commandInfo);
    }

    @Override
    public void run(Sender<? extends PlatformSender> sender, String[] arguments) {
        AbstractCommand.run0(this, sender, arguments, this::execute);
    }

    protected abstract void execute(S sender) throws Exception;
}
