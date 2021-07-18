package com.dfsek.terra.api.registry.meta;

import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.registry.Registry;
import com.dfsek.terra.api.util.TypeToken;

import java.lang.reflect.Type;

public interface RegistryHolder {
    default <T> Registry<T> getRegistry(Class<T> clazz) {
        return getRegistry((Type) clazz);
    }

    default <T> Registry<T> getRegistry(TypeToken<T> type) {
        return getRegistry(type.getType());
    }

    <T> Registry<T> getRegistry(Type type);

    default <T> CheckedRegistry<T> getCheckedRegistry(Class<T> clazz) throws IllegalStateException {
        return getCheckedRegistry((Type) clazz);
    }

    default <T> CheckedRegistry<T> getCheckedRegistry(TypeToken<T> type) throws IllegalStateException {
        return getCheckedRegistry(type.getType());
    }

    default <T> CheckedRegistry<T> getCheckedRegistry(Type type) throws IllegalStateException {
        throw new IllegalStateException("Cannot get checked registry.");
    }
}
