package com.dfsek.terra.api.event.events.config;

import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.tectonic.loading.object.ObjectTemplate;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.config.ConfigType;
import com.dfsek.terra.api.event.events.PackEvent;

import java.lang.reflect.Type;
import java.util.function.Supplier;


public class RegistrationEvent implements PackEvent {
    private final ConfigPack pack;
    
    public RegistrationEvent(ConfigPack pack) {
        this.pack = pack;
    }
    
    @Override
    public ConfigPack getPack() {
        return pack;
    }
    
    public RegistrationEvent registerConfigType(ConfigType<?, ?> type, String id, int priority) {
        pack.registerConfigType(type, id, priority);
        return this;
    }
    
    public <T> RegistrationEvent applyLoader(Type type, Supplier<ObjectTemplate<T>> loader) {
        pack.applyLoader(type, loader);
        return this;
    }
    
    public <T> RegistrationEvent applyLoader(Type type, TypeLoader<T> loader) {
        pack.applyLoader(type, loader);
        return this;
    }
}
