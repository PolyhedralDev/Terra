/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.registry;

import org.checkerframework.checker.nullness.qual.NonNull;

import com.dfsek.terra.api.registry.exception.DuplicateEntryException;
import com.dfsek.terra.api.registry.key.Keyed;
import com.dfsek.terra.api.registry.key.RegistryKey;


public interface CheckedRegistry<T> extends Registry<T> {
    /**
     * Add a value to this registry, checking whether it is present first.
     *
     * @param identifier Identifier to assign value.
     * @param value      Value to register.
     *
     * @throws DuplicateEntryException If an entry with the same identifier is already present.
     */
    void register(@NonNull RegistryKey identifier, @NonNull T value) throws DuplicateEntryException;
    
    @SuppressWarnings("unchecked")
    default void register(@NonNull Keyed<? extends T> value) throws DuplicateEntryException {
        register(value.getRegistryKey(), (T) value);
    }
}
