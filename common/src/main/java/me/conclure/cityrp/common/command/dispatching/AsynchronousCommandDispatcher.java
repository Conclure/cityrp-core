package me.conclure.cityrp.common.command.dispatching;

import com.google.common.base.Preconditions;
import me.conclure.cityrp.common.command.Command;
import me.conclure.cityrp.common.sender.Sender;
import me.conclure.cityrp.common.utility.concurrent.TaskCoordinator;

import java.util.concurrent.Executor;

public class AsynchronousCommandDispatcher<PlatformPlayer> implements CommandDispatcher<PlatformPlayer> {
    private final CommandDispatcher<PlatformPlayer> delegatingDispatcher;
    private final TaskCoordinator<? extends Executor> taskCoordinator;

    public AsynchronousCommandDispatcher(
            CommandDispatcher<PlatformPlayer> delegatingDispatcher,
            TaskCoordinator<? extends Executor> taskCoordinator
    ) {
        Preconditions.checkNotNull(taskCoordinator);
        Preconditions.checkNotNull(delegatingDispatcher);

        this.taskCoordinator = taskCoordinator;
        this.delegatingDispatcher = delegatingDispatcher;
    }

    public CommandDispatcher<PlatformPlayer> getDelegatingDispatcher() {
        return this.delegatingDispatcher;
    }

    public void dispatch(
            Command<? extends Sender<PlatformPlayer>, PlatformPlayer> command,
            Sender<? extends PlatformPlayer> sender,
            String[] args
    ) {
        Preconditions.checkNotNull(command);
        Preconditions.checkNotNull(sender);
        Preconditions.checkNotNull(args);
        this.taskCoordinator.executor().execute(() -> this.delegatingDispatcher.dispatch(command, sender, args));
    }
}
