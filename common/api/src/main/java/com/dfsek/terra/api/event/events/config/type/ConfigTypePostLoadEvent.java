package com.dfsek.terra.api.event.events.config.type;

import com.dfsek.terra.api.config.ConfigType;
import com.dfsek.terra.api.registry.CheckedRegistry;

public class ConfigTypePostLoadEvent extends ConfigTypeLoadEvent{
    public ConfigTypePostLoadEvent(ConfigType<?, ?> type, CheckedRegistry<?> registry) {
        super(type, registry);
    }
}