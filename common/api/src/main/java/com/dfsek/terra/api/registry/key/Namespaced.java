package com.dfsek.terra.api.registry.key;

public interface Namespaced {
    String namespace();

    default RegistryKey key(String id) {
        return RegistryKey.of(namespace(), id);
    }
}
