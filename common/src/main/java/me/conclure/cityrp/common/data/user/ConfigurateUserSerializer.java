package me.conclure.cityrp.common.data.user;

import me.conclure.cityrp.common.model.user.User;
import org.spongepowered.configurate.ConfigurationNode;

public interface ConfigurateUserSerializer {
    void serialize(ConfigurationNode node, User user);

    void deserialize(ConfigurationNode node, User user);
}
