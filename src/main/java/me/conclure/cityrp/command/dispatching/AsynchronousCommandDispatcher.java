package me.conclure.cityrp.command.dispatching;

import com.google.common.base.Preconditions;
import me.conclure.cityrp.command.Command;
import me.conclure.cityrp.sender.Sender;

import java.util.concurrent.Executor;

public class AsynchronousCommandDispatcher<SS,E extends Executor> implements CommandDispatcher<SS> {
    private final E executor;
    private final CommandDispatcher<SS> delegatingDispatcher;

    public AsynchronousCommandDispatcher(E executor, CommandDispatcher<SS> delegatingDispatcher) {
        Preconditions.checkNotNull(executor);
        Preconditions.checkNotNull(delegatingDispatcher);

        this.executor = executor;
        this.delegatingDispatcher = delegatingDispatcher;
    }

    public CommandDispatcher<SS> getDelegatingDispatcher() {
        return this.delegatingDispatcher;
    }

    public E getExecutor() {
        return this.executor;
    }

    public void dispatch(Command<? extends Sender<SS>,SS> command, Sender<SS> sender, String[] args) {
        Preconditions.checkNotNull(command);
        Preconditions.checkNotNull(sender);
        Preconditions.checkNotNull(args);
        this.executor.execute(() -> this.delegatingDispatcher.dispatch(command, sender, args));
    }
}
