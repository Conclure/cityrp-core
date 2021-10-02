package me.conclure.cityrp.common.data.user;

import me.conclure.cityrp.common.model.user.User;
import org.spongepowered.configurate.ConfigurationNode;

public class SimpleConfigurateUserSerializer implements ConfigurateUserSerializer {
    @Override
    public void serialize(ConfigurationNode node, User user) {
    }

    @Override
    public void deserialize(ConfigurationNode node, User user) {

    }
}
