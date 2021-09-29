package me.conclure.cityrp.common.data.user;

import me.conclure.cityrp.common.model.user.User;

import java.util.concurrent.CompletableFuture;

public interface UserDataManager<PlatformPlayer> {
    CompletableFuture<Void> load(User<PlatformPlayer> user);

    CompletableFuture<Void> save(User<PlatformPlayer> user);

}
