/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.event.events.config;

import com.dfsek.tectonic.api.config.template.ConfigTemplate;
import com.dfsek.tectonic.impl.abstraction.AbstractConfiguration;

import java.util.function.Consumer;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.config.ConfigType;
import com.dfsek.terra.api.event.events.FailThroughEvent;
import com.dfsek.terra.api.event.events.PackEvent;
import com.dfsek.terra.api.util.reflection.ReflectionUtil;


/**
 * Fired when each individual configuration is loaded.
 * <p>
 * Addons should listen to this event if they wish to add
 * config values to existing {@link ConfigType}s.
 */
public class ConfigurationLoadEvent implements PackEvent, FailThroughEvent {
    private final ConfigPack pack;
    private final AbstractConfiguration configuration;
    private final Consumer<ConfigTemplate> loader;
    private final ConfigType<?, ?> type;
    
    private final Object loaded;
    
    public ConfigurationLoadEvent(ConfigPack pack, AbstractConfiguration configuration, Consumer<ConfigTemplate> loader,
                                  ConfigType<?, ?> type, Object loaded) {
        this.pack = pack;
        this.configuration = configuration;
        this.loader = loader;
        this.type = type;
        this.loaded = loaded;
    }
    
    public <T extends ConfigTemplate> T load(T template) {
        loader.accept(template);
        return template;
    }
    
    public boolean is(Class<?> clazz) {
        return clazz.isAssignableFrom(type.getTypeKey().getRawType());
    }
    
    @Override
    public ConfigPack getPack() {
        return pack;
    }
    
    public AbstractConfiguration getConfiguration() {
        return configuration;
    }
    
    public ConfigType<?, ?> getType() {
        return type;
    }
    
    @SuppressWarnings("unchecked")
    public <T> T getLoadedObject(Class<T> clazz) {
        if(!clazz.isAssignableFrom(type.getTypeKey().getRawType()))
            throw new ClassCastException(
                    "Cannot assign object from loader of type " + ReflectionUtil.typeToString(type.getTypeKey().getType()) + " to class " +
                    clazz.getCanonicalName());
        return (T) loaded;
    }
    
    @SuppressWarnings("unchecked")
    public <T> T getLoadedObject() {
        return (T) loaded;
    }
}
