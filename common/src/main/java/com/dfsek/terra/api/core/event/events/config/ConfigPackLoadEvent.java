package com.dfsek.terra.api.core.event.events.config;

import com.dfsek.terra.api.core.event.Event;
import com.dfsek.terra.config.pack.ConfigPack;

public abstract class ConfigPackLoadEvent implements Event {
    private final ConfigPack pack;

    public ConfigPackLoadEvent(ConfigPack pack) {
        this.pack = pack;
    }

    public ConfigPack getPack() {
        return pack;
    }
}
