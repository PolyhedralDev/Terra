package com.dfsek.terra.api.event.events.config;

import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.terra.api.config.ConfigPack;

/**
 * Called when a config pack has finished loading.
 */
public class ConfigPackPostLoadEvent extends ConfigPackLoadEvent {
    public ConfigPackPostLoadEvent(ConfigPack pack, ExceptionalConsumer<ConfigTemplate> loader) {
        super(pack, loader);
    }
}
