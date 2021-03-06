package me.conclure.cityrp.common.data.user;

import me.conclure.cityrp.common.model.user.User;

import java.util.concurrent.CompletableFuture;

public interface UserDataStorage {
    UserDataController getUserDataController();

    CompletableFuture<Void> load(User user);

    CompletableFuture<Void> save(User user);

}
