package me.conclure.cityrp.common.model.user;

import java.util.UUID;
import java.util.function.Function;

public interface UserFactory {
    User create(UUID uniqueId);
}
