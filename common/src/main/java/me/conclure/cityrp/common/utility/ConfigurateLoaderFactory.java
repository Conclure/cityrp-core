package me.conclure.cityrp.common.utility;

import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.loader.ConfigurationLoader;

import java.nio.file.Path;

@FunctionalInterface
public interface ConfigurateLoaderFactory {

    ConfigurationLoader<? extends ConfigurationNode> loader(Path path);
}
