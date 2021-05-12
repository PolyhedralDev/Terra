package com.dfsek.terra.api.event.events.config;

import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.tectonic.exception.ConfigException;
import com.dfsek.terra.config.pack.ConfigPack;

/**
 * Called before a config pack's registries are filled. At this point, the pack manifest has been loaded, and all registries are empty.
 */
public class ConfigPackPreLoadEvent extends ConfigPackLoadEvent {
    private final ExceptionalConsumer<ConfigTemplate> configLoader;

    public ConfigPackPreLoadEvent(ConfigPack pack, ExceptionalConsumer<ConfigTemplate> configLoader) {
        super(pack);
        this.configLoader = configLoader;
    }

    /**
     * Load a custom {@link ConfigTemplate} using the pack manifest.
     *
     * @param template Template to register.
     */
    public void loadTemplate(ConfigTemplate template) throws ConfigException {
        configLoader.accept(template);
    }

    public interface ExceptionalConsumer<T extends ConfigTemplate> {
        void accept(T value) throws ConfigException;
    }
}
