/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.registry;

import org.jetbrains.annotations.NotNull;

import com.dfsek.terra.api.registry.exception.DuplicateEntryException;
import com.dfsek.terra.api.registry.key.Keyed;
import com.dfsek.terra.api.registry.key.RegistryKey;


public interface OpenRegistry<T> extends Registry<T> {
    /**
     * Add a value to this registry.
     *
     * @param identifier Identifier to assign value.
     * @param value      Value to register.
     */
    boolean register(@NotNull RegistryKey identifier, @NotNull T value);
    
    @SuppressWarnings("unchecked")
    default boolean register(@NotNull Keyed<? extends T> value) {
        return register(value.getRegistryKey(), (T) value);
    }
    
    /**
     * Add a value to this registry, checking whether it is present first.
     *
     * @param identifier Identifier to assign value.
     * @param value      Value to register.
     *
     * @throws DuplicateEntryException If an entry with the same identifier is already present.
     */
    void registerChecked(@NotNull RegistryKey identifier, @NotNull T value) throws DuplicateEntryException;
    
    @SuppressWarnings("unchecked")
    default void registerChecked(@NotNull Keyed<? extends T> value) {
        registerChecked(value.getRegistryKey(), (T) value);
    }
    
    /**
     * Clears all entries from the registry.
     */
    void clear();
}
