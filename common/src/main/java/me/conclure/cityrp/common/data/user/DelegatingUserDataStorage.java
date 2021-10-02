package me.conclure.cityrp.common.data.user;

import me.conclure.cityrp.common.model.user.User;
import me.conclure.cityrp.common.utility.concurrent.TaskCoordinator;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class DelegatingUserDataStorage implements UserDataStorage {
    private final TaskCoordinator<? extends Executor> taskCoordinator;
    private final UserDataController userDataController;

    public DelegatingUserDataStorage(
            TaskCoordinator<? extends Executor> taskCoordinator,
            UserDataController userDataController
    ) {
        this.taskCoordinator = taskCoordinator;
        this.userDataController = userDataController;
    }

    @Override
    public UserDataController getUserDataController() {
        return this.userDataController;
    }

    @Override
    public CompletableFuture<Void> load(User user) {
        return this.taskCoordinator.run(() -> this.userDataController.load(user));
    }

    @Override
    public CompletableFuture<Void> save(User user) {
        return this.taskCoordinator.run(() -> this.userDataController.save(user));
    }
}
