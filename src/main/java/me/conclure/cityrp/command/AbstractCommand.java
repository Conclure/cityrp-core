package me.conclure.cityrp.command;

import com.google.common.base.Preconditions;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;

public abstract class AbstractCommand<S extends CommandSender> implements Command<S> {
    private final CommandInfo<S> commandInfo;

    protected AbstractCommand(CommandInfo<S> commandInfo) {
        Preconditions.checkNotNull(commandInfo);
        this.commandInfo = commandInfo;
    }

    @Override
    public boolean acceptsSender(CommandSender sender) {
        Preconditions.checkNotNull(sender);
        return this.commandInfo.getSenderType().isInstance(sender);
    }

    @Override
    public boolean acceptsSenderType(Class<? extends CommandSender> type) {
        Preconditions.checkNotNull(type);
        return type.isAssignableFrom(this.commandInfo.getSenderType());
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        Preconditions.checkNotNull(sender);
        return sender.hasPermission(this.commandInfo.getPermission());
    }


    @Override
    public CommandInfo<S> getInfo() {
        return this.commandInfo;
    }

    @Override
    public void run(CommandSender sender, String[] arguments) {
        Preconditions.checkNotNull(sender);
        Preconditions.checkNotNull(arguments);

        boolean commandHasPermission = !this.commandInfo.requiresPermission();
        boolean senderHasNotPermission = !this.hasPermission(sender);

        if (commandHasPermission && senderHasNotPermission) {
            sender.sendMessage(Component.text("You don't have permission!"));
            return;
        }

        boolean notAcceptsSender = !this.acceptsSender(sender);
        if (notAcceptsSender) {
            sender.sendMessage(Component.text("Your sender type is disallowed for this command!"));
            return;
        }

        try {
            this.execute(this.commandInfo.getSenderType().cast(sender),arguments);
        } catch (Exception e) {
            throw new CommandException(e);
        }
    }

    protected abstract void execute(S sender, String[] arguments) throws Exception;

}
