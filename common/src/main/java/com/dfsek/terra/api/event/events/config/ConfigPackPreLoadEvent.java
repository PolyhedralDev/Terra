package com.dfsek.terra.api.event.events.config;

import com.dfsek.terra.config.pack.ConfigPack;

/**
 * Called before a config pack's registries are filled. At this point, the pack manifest has been loaded, and all registries are empty.
 */
public class ConfigPackPreLoadEvent extends ConfigPackLoadEvent {
    public ConfigPackPreLoadEvent(ConfigPack pack) {
        super(pack);
    }
}
