package com.dfsek.terra.api.properties;

public class PropertyKey<T extends Properties> {
    protected final int key;
    private final Class<T> clazz;
    
    protected PropertyKey(int key, Class<T> clazz) {
        this.key = key;
        this.clazz = clazz;
    }
    
    public Class<T> getTypeClass() {
        return clazz;
    }
}
