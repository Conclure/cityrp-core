package me.conclure.cityrp.common.model.user;

import java.util.UUID;

public interface UserScrubber {

    void registerUsage(UUID uniqueId);

    void clean(UUID uniqueId);

    Runnable getTask();
}
