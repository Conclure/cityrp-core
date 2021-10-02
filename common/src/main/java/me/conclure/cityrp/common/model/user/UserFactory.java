package me.conclure.cityrp.common.model.user;

import java.util.UUID;

public interface UserFactory {
    User create(UUID uniqueId);
}
