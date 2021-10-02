package me.conclure.cityrp.common.data.user;

import me.conclure.cityrp.common.model.user.User;

public interface UserDataController {
    void save(User user);

    void load(User user);
}
