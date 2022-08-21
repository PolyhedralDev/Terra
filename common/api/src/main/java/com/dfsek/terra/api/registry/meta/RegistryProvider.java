package com.dfsek.terra.api.registry.meta;

import com.dfsek.terra.api.registry.Registry;
import com.dfsek.terra.api.util.reflection.TypeKey;


public interface RegistryProvider {
    default <T> Registry<T> createRegistry(Class<T> clazz) {
        return createRegistry(TypeKey.of(clazz));
    }
    
    <T> Registry<T> createRegistry(TypeKey<T> type);
}
