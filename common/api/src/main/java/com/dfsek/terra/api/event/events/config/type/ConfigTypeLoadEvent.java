package com.dfsek.terra.api.event.events.config.type;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.config.ConfigType;
import com.dfsek.terra.api.event.events.PackEvent;
import com.dfsek.terra.api.registry.CheckedRegistry;

public abstract class ConfigTypeLoadEvent implements PackEvent {
    private final ConfigType<?, ?> type;
    private final CheckedRegistry<?> registry;

    public ConfigTypeLoadEvent(ConfigType<?, ?> type, CheckedRegistry<?> registry) {
        this.type = type;
        this.registry = registry;
    }

    @Override
    public ConfigPack getPack() {
        return null;
    }

    @SuppressWarnings("unchecked")
    public <T> CheckedRegistry<T> getRegistry(Class<T> clazz) {
        if(!clazz.isAssignableFrom(type.getTypeClass()))
            throw new ClassCastException("Cannot assign object from loader of type " + type.getTypeClass().getCanonicalName() + " to class " + clazz.getCanonicalName());
        return (CheckedRegistry<T>) registry;
    }
}
