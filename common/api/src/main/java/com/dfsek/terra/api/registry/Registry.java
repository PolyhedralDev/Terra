/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.registry;

import com.dfsek.tectonic.api.loader.type.TypeLoader;

import com.dfsek.terra.api.registry.key.Keyed;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.api.util.reflection.TypeKey;


public interface Registry<T> extends TypeLoader<T> {
    /**
     * Get a value from the registry.
     *
     * @param key Identifier of value.
     *
     * @return Value matching the identifier, {@code null} if no value is present.
     */
    @Contract(pure = true)
    Optional<T> get(@NotNull RegistryKey key);
    
    /**
     * Check if the registry contains a value.
     *
     * @param key Identifier of value.
     *
     * @return Whether the registry contains the value.
     */
    @Contract(pure = true)
    boolean contains(@NotNull RegistryKey key);
    
    /**
     * Perform the given action for every value in the registry.
     *
     * @param consumer Action to perform on value.
     */
    void forEach(@NotNull Consumer<T> consumer);
    
    /**
     * Perform an action for every key-value pair in the registry.
     *
     * @param consumer Action to perform on pair.
     */
    void forEach(@NotNull BiConsumer<RegistryKey, T> consumer);
    
    /**
     * Get the entries of this registry as a {@link Set}.
     *
     * @return Set containing all entries.
     */
    @NotNull
    @Contract(pure = true)
    Set<T> entries();
    
    /**
     * Add a value to this registry.
     *
     * @param identifier Identifier to assign value.
     * @param value      Value to register.
     */
    @Contract(pure = true, value = "_,_->new")
    Registry<T> register(@NotNull RegistryKey identifier, @NotNull T value);
    
    @SuppressWarnings("unchecked")
    @Contract(pure = true, value = "_->new")
    default Registry<T> register(@NotNull Keyed<? extends T> value) {
        return register(value.getRegistryKey(), (T) value);
    }
    
    /**
     * Get all the keys in this registry.
     *
     * @return Keys in registry
     */
    @NotNull
    @Contract(pure = true)
    Set<RegistryKey> keys();
    
    TypeKey<T> getType();
    
    default Class<? super T> getRawType() {
        return getType().getRawType();
    }
    
    Map<RegistryKey, T> getID(String id);
    
    Map<RegistryKey, T> getNamespace(String namespace);
}
