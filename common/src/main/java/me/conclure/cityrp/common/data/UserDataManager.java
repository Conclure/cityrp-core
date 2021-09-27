package me.conclure.cityrp.common.data;

import java.util.concurrent.CompletableFuture;

public interface UserDataManager {
    CompletableFuture<Void> load(User user);

    CompletableFuture<Void> save(User user);

}
