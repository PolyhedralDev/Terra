package com.dfsek.terra.api.registry.key;

public interface Keyed<T extends Keyed<T>> extends Namespaced, StringIdentifiable {
    RegistryKey getRegistryKey();
    
    @Override
    default String getNamespace() {
        return getRegistryKey().getNamespace();
    }
    
    @Override
    default String getID() {
        return getRegistryKey().getID();
    }
}
