package com.dfsek.terra.api.event.events.config;

import com.dfsek.tectonic.abstraction.AbstractConfiguration;
import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.config.ConfigType;
import com.dfsek.terra.api.event.events.PackEvent;

import java.util.function.Consumer;

/**
 * Fired when each individual configuration is loaded.
 */
public class ConfigLoadEvent implements PackEvent {
    private final ConfigPack pack;
    private final AbstractConfiguration configuration;
    private final Consumer<ConfigTemplate> loader;
    private final ConfigType<?, ?> type;

    public ConfigLoadEvent(ConfigPack pack, AbstractConfiguration configuration, Consumer<ConfigTemplate> loader, ConfigType<?, ?> type) {
        this.pack = pack;
        this.configuration = configuration;
        this.loader = loader;
        this.type = type;
    }

    @Override
    public ConfigPack getPack() {
        return pack;
    }

    public AbstractConfiguration getConfiguration() {
        return configuration;
    }

    public <T extends ConfigTemplate> T load(T template) {
        loader.accept(template);
        return template;
    }

    public ConfigType<?, ?> getType() {
        return type;
    }
}
