package me.conclure.cityrp.command.abstraction;

import com.google.common.base.Preconditions;
import com.google.common.reflect.TypeToken;
import me.conclure.cityrp.command.Command;
import me.conclure.cityrp.command.CommandException;
import me.conclure.cityrp.command.CommandInfo;
import me.conclure.cityrp.sender.Sender;
import me.conclure.cityrp.utility.function.ThrowingConsumer;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;

public abstract class AbstractCommand<S extends Sender<SS>,SS> implements Command<S,SS> {
    private final CommandInfo<S,SS> commandInfo;

    protected AbstractCommand(CommandInfo<S,SS> commandInfo) {
        Preconditions.checkNotNull(commandInfo);

        this.commandInfo = commandInfo;
    }

    static <S extends Sender<SS>,SS> void run0(
            AbstractCommand<S,SS> command,
            Sender<SS> sender,
            String[] arguments,
            ThrowingConsumer<S, Exception> executor
    ) {
        Preconditions.checkNotNull(command);
        Preconditions.checkNotNull(sender);
        Preconditions.checkNotNull(arguments);
        Preconditions.checkNotNull(executor);

        boolean requiresPermission = command.commandInfo.requiresPermission();
        boolean senderHasNotPermission = !command.hasPermission(sender);
        boolean hasNotPermission = requiresPermission && senderHasNotPermission;

        if (hasNotPermission) {
            sender.sendMessage(Component.text("You don't have permission!"));
            return;
        }

        boolean notAcceptsSender = !command.acceptsSender(sender);
        if (notAcceptsSender) {
            sender.sendMessage(Component.text("Your sender type is disallowed for this command!"));
            return;
        }

        try {
            executor.accept((S) sender);
        } catch (Exception e) {
            throw new CommandException(e);
        }
    }

    @Override
    public boolean acceptsSender(Sender<SS> sender) {
        Preconditions.checkNotNull(sender);

        return this.commandInfo.getSenderType().isSupertypeOf(sender.getClass());
    }

    @Override
    public boolean acceptsSenderType(Class<? extends Sender<SS>> type) {
        Preconditions.checkNotNull(type);

        return this.commandInfo.getSenderType().isSupertypeOf(type);
    }

    @Override
    public boolean hasPermission(Sender<SS> sender) {
        Preconditions.checkNotNull(sender);

        boolean notRequiresPermission = !this.commandInfo.requiresPermission();

        if (notRequiresPermission) {
            return true;
        }

        return sender.hasPermission(this.commandInfo.getPermission());
    }


    @Override
    public CommandInfo<S,SS> getInfo() {
        return this.commandInfo;
    }

}
