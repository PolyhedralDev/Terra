package com.dfsek.terra.api.registry.meta;

import com.dfsek.tectonic.loading.TypeLoader;

import java.util.function.Function;

import com.dfsek.terra.api.registry.OpenRegistry;


/**
 * Helpers to avoid creating entire registry implementations for simple overrides.
 */
public interface RegistryFactory {
    /**
     * Create a generic OpenRegistry.
     *
     * @param <T> Type of registry.
     *
     * @return New OpenRegistry
     */
    <T> OpenRegistry<T> create();
    
    /**
     * Create an OpenRegistry with custom {@link TypeLoader}
     *
     * @param loader Function to create loader.
     * @param <T>    Type of registry.
     *
     * @return New OpenRegistry.
     */
    <T> OpenRegistry<T> create(Function<OpenRegistry<T>, TypeLoader<T>> loader);
}
