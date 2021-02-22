package com.dfsek.terra.api.event.events.config;

import com.dfsek.terra.config.pack.ConfigPack;

/**
 * Called when a config pack has finished loading.
 */
public class ConfigPackPostLoadEvent extends ConfigPackLoadEvent {
    public ConfigPackPostLoadEvent(ConfigPack pack) {
        super(pack);
    }
}
