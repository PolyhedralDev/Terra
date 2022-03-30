package com.dfsek.terra.api.registry.meta;

import java.lang.reflect.Type;

import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.util.reflection.TypeKey;


public interface CheckedRegistryHolder extends RegistryHolder {
    default <T> CheckedRegistry<T> getCheckedRegistry(Class<T> clazz) throws IllegalStateException {
        return getCheckedRegistry((Type) clazz);
    }
    
    default <T> CheckedRegistry<T> getCheckedRegistry(TypeKey<T> type) throws IllegalStateException {
        return getCheckedRegistry(type.getType());
    }
    
    <T> CheckedRegistry<T> getCheckedRegistry(Type type);
}
