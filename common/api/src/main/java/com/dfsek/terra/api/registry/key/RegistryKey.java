package com.dfsek.terra.api.registry.key;

import java.util.Objects;
import java.util.regex.Pattern;


public final class RegistryKey implements StringIdentifiable, Namespaced {
    private static final Pattern ID_PATTERN = Pattern.compile("^[a-zA-Z0-9_-]*$");
    private final String namespace;
    private final String id;
    
    private RegistryKey(String namespace, String id) {
        if(!ID_PATTERN.matcher(namespace).matches()) {
            throw new IllegalArgumentException(
                    "Namespace must only contain alphanumeric characters, hyphens, and underscores. \"" + namespace +
                    "\" is not a valid namespace.");
        }
        
        if(!ID_PATTERN.matcher(id).matches()) {
            throw new IllegalArgumentException(
                    "ID must only contain alphanumeric characters, hyphens, and underscores. \"" + id +
                    "\" is not a valid ID.");
        }
        
        this.namespace = namespace;
        this.id = id;
    }
    
    public static RegistryKey parse(String key) {
        if(key.chars().filter(c -> c == ':').count() != 1) {
            throw new IllegalArgumentException("Malformed RegistryKey: " + key);
        }
        String namespace = key.substring(0, key.indexOf(":"));
        String id = key.substring(key.indexOf(":") + 1);
        
        return new RegistryKey(namespace, id);
    }
    
    public static RegistryKey of(String namespace, String id) {
        return new RegistryKey(namespace, id);
    }
    
    @Override
    public String getNamespace() {
        return namespace;
    }
    
    @Override
    public String getID() {
        return id;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(namespace, id);
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof RegistryKey that) {
            return this.id.equals(that.id) && this.namespace.equals(that.namespace);
        }
        return false;
    }
    
    @Override
    public String toString() {
        return namespace + ":" + id;
    }
}
