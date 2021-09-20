package me.conclure.cityrp.command.dispatching;

import com.google.common.base.Preconditions;
import me.conclure.cityrp.command.Command;
import org.bukkit.command.CommandSender;

import java.util.concurrent.Executor;

public class AsynchronousCommandDispatcher<E extends Executor> implements CommandDispatcher {
    private final E executor;
    private final CommandDispatcher delegatingDispatcher;

    public AsynchronousCommandDispatcher(E executor, CommandDispatcher delegatingDispatcher) {
        Preconditions.checkNotNull(executor);
        Preconditions.checkNotNull(delegatingDispatcher);

        this.executor = executor;
        this.delegatingDispatcher = delegatingDispatcher;
    }

    public CommandDispatcher getDelegatingDispatcher() {
        return this.delegatingDispatcher;
    }

    public E getExecutor() {
        return this.executor;
    }

    public <S extends CommandSender> void dispatch(Command<S> command, CommandSender sender, String[] args) {
        Preconditions.checkNotNull(command);
        Preconditions.checkNotNull(sender);
        Preconditions.checkNotNull(args);
        this.executor.execute(() -> this.delegatingDispatcher.dispatch(command,sender,args));
    }
}
