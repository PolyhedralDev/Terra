package com.dfsek.terra.api.event.events.config;

import com.dfsek.terra.api.event.events.PackEvent;
import com.dfsek.terra.config.pack.ConfigPack;

public abstract class ConfigPackLoadEvent implements PackEvent {
    private final ConfigPack pack;

    public ConfigPackLoadEvent(ConfigPack pack) {
        this.pack = pack;
    }

    @Override
    public ConfigPack getPack() {
        return pack;
    }
}
