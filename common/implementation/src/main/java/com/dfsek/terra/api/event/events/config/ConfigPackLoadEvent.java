package com.dfsek.terra.api.event.events.config;

import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.tectonic.exception.ConfigException;
import com.dfsek.terra.api.event.events.PackEvent;
import com.dfsek.terra.config.pack.ConfigPack;

/**
 * An event related to the loading process of config packs.
 */
public abstract class ConfigPackLoadEvent implements PackEvent {
    private final ConfigPack pack;
    private final ExceptionalConsumer<ConfigTemplate> configLoader;

    public ConfigPackLoadEvent(ConfigPack pack, ExceptionalConsumer<ConfigTemplate> configLoader) {
        this.pack = pack;
        this.configLoader = configLoader;
    }

    @Override
    public ConfigPack getPack() {
        return pack;
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
