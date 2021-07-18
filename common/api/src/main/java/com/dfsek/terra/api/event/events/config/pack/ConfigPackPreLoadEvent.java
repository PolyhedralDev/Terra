package com.dfsek.terra.api.event.events.config.pack;

import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.terra.api.config.ConfigPack;

/**
 * Called before a config pack's registries are filled.
 */
public class ConfigPackPreLoadEvent extends ConfigPackLoadEvent {
    public ConfigPackPreLoadEvent(ConfigPack pack, ExceptionalConsumer<ConfigTemplate> configLoader) {
        super(pack, configLoader);
    }
}
