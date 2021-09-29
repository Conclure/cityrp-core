package me.conclure.cityrp.common.utility;

import java.util.UUID;

public interface PlayerObtainer<PlatformPlayer> {
    PlatformPlayer getByUniqueId(UUID uniqueId);

    PlatformPlayer getByName(String name);
}
