package me.conclure.cityrp.common.plugin;

public interface PluginLifecycle {
    String PLUGIN_NAME = "CityRP";
    String PLUGIN_NAME_LOWER = "cityrp";

    void load();

    void enable();

    void disable();
}
