package com.dfsek.terra.api.registry.key;

public interface Namespaced {
    String getNamespace();
    
    default RegistryKey getKey(String key) {
        return new RegistryKey() {
            @Override
            public String getNamespace() {
                return Namespaced.this.getNamespace();
            }
    
            @Override
            public String getID() {
                return key;
            }
        };
    }
}
