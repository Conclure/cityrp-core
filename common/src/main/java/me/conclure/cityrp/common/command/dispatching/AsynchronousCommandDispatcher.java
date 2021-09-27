package me.conclure.cityrp.common.command.dispatching;

import com.google.common.base.Preconditions;
import me.conclure.cityrp.common.command.Command;
import me.conclure.cityrp.common.sender.Sender;
import me.conclure.cityrp.common.utility.concurrent.TaskCoordinator;

import java.util.concurrent.Executor;

public class AsynchronousCommandDispatcher<SS> implements CommandDispatcher<SS> {
    private final CommandDispatcher<SS> delegatingDispatcher;
    private final TaskCoordinator<? extends Executor> taskCoordinator;

    public AsynchronousCommandDispatcher(
            CommandDispatcher<SS> delegatingDispatcher,
            TaskCoordinator<? extends Executor> taskCoordinator
    ) {
        Preconditions.checkNotNull(taskCoordinator);
        Preconditions.checkNotNull(delegatingDispatcher);

        this.taskCoordinator = taskCoordinator;
        this.delegatingDispatcher = delegatingDispatcher;
    }

    public CommandDispatcher<SS> getDelegatingDispatcher() {
        return this.delegatingDispatcher;
    }

    public void dispatch(Command<? extends Sender<SS>,SS> command, Sender<? extends SS> sender, String[] args) {
        Preconditions.checkNotNull(command);
        Preconditions.checkNotNull(sender);
        Preconditions.checkNotNull(args);
        this.taskCoordinator.executor().execute(() -> this.delegatingDispatcher.dispatch(command, sender, args));
    }
}
