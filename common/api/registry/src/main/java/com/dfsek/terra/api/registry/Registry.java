/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.registry;

import com.dfsek.tectonic.api.loader.type.TypeLoader;

import com.dfsek.terra.api.registry.exception.NoSuchEntryException;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;


public interface Registry<T> extends TypeLoader<T> {
    /**
     * Get a value from the registry.
     *
     * @param identifier Identifier of value.
     *
     * @return Value matching the identifier, {@code null} if no value is present.
     */
    @Contract(pure = true)
    Optional<T> get(@NotNull String identifier);
    
    /**
     * Check if the registry contains a value.
     *
     * @param identifier Identifier of value.
     *
     * @return Whether the registry contains the value.
     */
    @Contract(pure = true)
    boolean contains(@NotNull String identifier);
    
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
    void forEach(@NotNull BiConsumer<String, T> consumer);
    
    /**
     * Get the entries of this registry as a {@link Set}.
     *
     * @return Set containing all entries.
     */
    @NotNull
    @Contract(pure = true)
    Collection<T> entries();
    
    /**
     * Get all the keys in this registry.
     *
     * @return Keys in registry
     */
    @NotNull
    @Contract(pure = true)
    Set<String> keys();
}
