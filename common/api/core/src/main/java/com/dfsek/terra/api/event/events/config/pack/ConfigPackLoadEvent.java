/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.event.events.config.pack;

import com.dfsek.tectonic.api.config.template.ConfigTemplate;
import com.dfsek.tectonic.api.exception.ConfigException;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.event.events.FailThroughEvent;
import com.dfsek.terra.api.event.events.PackEvent;


/**
 * An event related to the loading process of config packs.
 */
public abstract class ConfigPackLoadEvent implements PackEvent, FailThroughEvent {
    private final ConfigPack pack;
    private final ExceptionalConsumer<ConfigTemplate> configLoader;
    
    public ConfigPackLoadEvent(ConfigPack pack, ExceptionalConsumer<ConfigTemplate> configLoader) {
        this.pack = pack;
        this.configLoader = configLoader;
    }
    
    /**
     * Load a custom {@link ConfigTemplate} using the pack manifest.
     *
     * @param template Template to register.
     */
    public <T extends ConfigTemplate> T loadTemplate(T template) throws ConfigException {
        configLoader.accept(template);
        return template;
    }
    
    @Override
    public ConfigPack getPack() {
        return pack;
    }
    
    public interface ExceptionalConsumer<T extends ConfigTemplate> {
        void accept(T value) throws ConfigException;
    }
}
