/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.event.events.config.type;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.config.ConfigType;
import com.dfsek.terra.api.event.events.FailThroughEvent;
import com.dfsek.terra.api.event.events.PackEvent;
import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.util.reflection.ReflectionUtil;


public abstract class ConfigTypeLoadEvent implements PackEvent, FailThroughEvent {
    private final ConfigType<?, ?> type;
    private final CheckedRegistry<?> registry;
    
    private final ConfigPack pack;
    
    public ConfigTypeLoadEvent(ConfigType<?, ?> type, CheckedRegistry<?> registry, ConfigPack pack) {
        this.type = type;
        this.registry = registry;
        this.pack = pack;
    }
    
    public boolean is(Class<?> clazz) {
        return clazz.isAssignableFrom(type.getTypeKey().getRawType());
    }
    
    @Override
    public ConfigPack getPack() {
        return pack;
    }
    
    @SuppressWarnings("unchecked")
    public <T> CheckedRegistry<T> getRegistry(Class<T> clazz) {
        if(!clazz.isAssignableFrom(type.getTypeKey().getRawType()))
            throw new ClassCastException(
                    "Cannot assign object from loader of type " + ReflectionUtil.typeToString(type.getTypeKey().getType()) + " to class " +
                    clazz.getCanonicalName());
        return (CheckedRegistry<T>) registry;
    }
}
