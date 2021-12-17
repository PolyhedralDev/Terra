/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.registry.meta;

import java.lang.reflect.Type;

import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.registry.Registry;
import com.dfsek.terra.api.util.reflection.TypeKey;


public interface RegistryHolder {
    default <T> Registry<T> getRegistry(Class<T> clazz) {
        return getRegistry((Type) clazz);
    }
    
    default <T> Registry<T> getRegistry(TypeKey<T> type) {
        return getRegistry(type.getType());
    }
    
    <T> Registry<T> getRegistry(Type type);
    
    default <T> CheckedRegistry<T> getCheckedRegistry(Class<T> clazz) throws IllegalStateException {
        return getCheckedRegistry((Type) clazz);
    }
    
    default <T> CheckedRegistry<T> getCheckedRegistry(TypeKey<T> type) throws IllegalStateException {
        return getCheckedRegistry(type.getType());
    }
    
    default <T> CheckedRegistry<T> getCheckedRegistry(Type type) throws IllegalStateException {
        throw new UnsupportedOperationException("Cannot get checked registry.");
    }
}
