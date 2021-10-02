package me.conclure.cityrp.common.model.user;

import me.conclure.cityrp.common.utility.PlayerUUIDHelper;
import me.conclure.cityrp.common.utility.collections.TimeoutSet;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * LuckPerms (MIT License)
 *
 * @author lucko
 */
public class SimpleUserScrubber implements UserScrubber, Runnable {
    private final UserRepository userRepository;
    private final TimeoutSet<UUID> recentlyUsed;
    private final PlayerUUIDHelper playerUUIDHelper;

    public SimpleUserScrubber(
            UserRepository userRepository,
            PlayerUUIDHelper playerUUIDHelper,
            TimeUnit unit,
            long timeoutDuration
    ) {
        this.userRepository = userRepository;
        this.recentlyUsed = new TimeoutSet<>(timeoutDuration, unit);
        this.playerUUIDHelper = playerUUIDHelper;
    }

    @Override
    public void registerUsage(UUID uniqueId) {
        this.recentlyUsed.add(uniqueId);
    }

    @Override
    public void clean(UUID uniqueId) {
        if (this.recentlyUsed.contains(uniqueId) || this.playerUUIDHelper.isOnline(uniqueId)) {
            return;
        }

        if (!this.userRepository.contains(uniqueId)) {
            return;
        }

        this.userRepository.discard(uniqueId);
    }

    @Override
    public Runnable getTask() {
        return this;
    }

    @Override
    public void run() {
        for (User user : this.userRepository) {
            this.clean(user.getUniqueId());
        }
    }
}
