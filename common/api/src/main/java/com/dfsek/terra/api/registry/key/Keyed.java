package com.dfsek.terra.api.registry.key;

public interface Keyed extends Namespaced, StringIdentifiable {
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
