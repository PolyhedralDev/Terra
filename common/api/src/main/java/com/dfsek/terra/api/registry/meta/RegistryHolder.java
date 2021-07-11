package com.dfsek.terra.api.registry.meta;

import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.registry.Registry;

public interface RegistryHolder {
    <T> Registry<T> getRegistry(Class<T> clazz);

    <T> CheckedRegistry<T> getCheckedRegistry(Class<T> clazz) throws IllegalStateException;
}
