package com.dfsek.terra.api.registry.meta;

import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.util.reflection.TypeKey;


public interface RegistryProvider {
    default <T> CheckedRegistry<T> getOrCreateRegistry(Class<T> clazz) {
        return getOrCreateRegistry(TypeKey.of(clazz));
    }
    
    <T> CheckedRegistry<T> getOrCreateRegistry(TypeKey<T> type);
}
