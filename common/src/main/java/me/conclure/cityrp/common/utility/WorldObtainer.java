package me.conclure.cityrp.common.utility;

import java.util.UUID;

public interface WorldObtainer<PlatformWorld> {
    PlatformWorld getByName(String name);

    PlatformWorld getById(UUID uniqueId);
}
