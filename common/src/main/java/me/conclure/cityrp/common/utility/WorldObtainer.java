package me.conclure.cityrp.common.utility;

import java.util.UUID;

public interface WorldObtainer<W> {
    W getByName(String name);

    W getById(UUID uniqueId);
}
