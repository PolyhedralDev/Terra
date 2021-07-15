package com.dfsek.terra.api.event.events.config;

import com.dfsek.tectonic.abstraction.AbstractConfiguration;
import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.config.ConfigType;
import com.dfsek.terra.api.event.events.PackEvent;

import java.util.function.Consumer;

/**
 * Fired when each individual configuration is loaded.
 * <p>
 * Addons should listen to this event if they wish to add
 * config values to existing {@link ConfigType}s.
 */
public class ConfigurationLoadEvent implements PackEvent {
    private final ConfigPack pack;
    private final AbstractConfiguration configuration;
    private final Consumer<ConfigTemplate> loader;
    private final ConfigType<?, ?> type;

    private final Object loaded;

    public ConfigurationLoadEvent(ConfigPack pack, AbstractConfiguration configuration, Consumer<ConfigTemplate> loader, ConfigType<?, ?> type, Object loaded) {
        this.pack = pack;
        this.configuration = configuration;
        this.loader = loader;
        this.type = type;
        this.loaded = loaded;
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

    @SuppressWarnings("unchecked")
    public <T> T getLoadedObject(Class<T> clazz) {
        if(!clazz.isAssignableFrom(type.getTypeClass()))
            throw new ClassCastException("Cannot assign object from loader of type " + type.getTypeClass().getCanonicalName() + " to class " + clazz.getCanonicalName());
        return (T) loaded;
    }
}
