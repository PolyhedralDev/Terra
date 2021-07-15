package com.dfsek.terra.api.event.events.config.type;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.config.ConfigType;
import com.dfsek.terra.api.event.events.PackEvent;
import com.dfsek.terra.api.registry.CheckedRegistry;

public abstract class ConfigTypeLoadEvent implements PackEvent {
    private final ConfigType<?, ?> type;
    private final CheckedRegistry<?> registry;

    private final ConfigPack pack;

    public ConfigTypeLoadEvent(ConfigType<?, ?> type, CheckedRegistry<?> registry, ConfigPack pack) {
        this.type = type;
        this.registry = registry;
        this.pack = pack;
    }

    @Override
    public ConfigPack getPack() {
        return pack;
    }

    public boolean is(Class<?> clazz) {
        return clazz.isAssignableFrom(type.getTypeClass());
    }

    @SuppressWarnings("unchecked")
    public <T> CheckedRegistry<T> getRegistry(Class<T> clazz) {
        if(!clazz.isAssignableFrom(type.getTypeClass()))
            throw new ClassCastException("Cannot assign object from loader of type " + type.getTypeClass().getCanonicalName() + " to class " + clazz.getCanonicalName());
        return (CheckedRegistry<T>) registry;
    }
}
