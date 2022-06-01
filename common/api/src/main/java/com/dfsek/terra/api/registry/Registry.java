/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.registry;

import com.dfsek.tectonic.api.loader.type.TypeLoader;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

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
    Collection<T> entries();
    
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
    
    default Optional<T> getByID(String id) {
        return getByID(id, map -> {
            if(map.isEmpty()) return Optional.empty();
            if(map.size() == 1) {
                return map.values().stream().findFirst(); // only one value.
            }
            throw new IllegalArgumentException("ID \"" + id + "\" is ambiguous; matches: " + map
                    .keySet()
                    .stream()
                    .map(RegistryKey::toString)
                    .reduce("", (a, b) -> a + "\n - " + b));
        });
    }
    
    default Collection<T> getAllWithID(String id) {
        return getMatches(id).values();
    }
    
    Map<RegistryKey, T> getMatches(String id);
    
    default Optional<T> getByID(String attempt, Function<Map<RegistryKey, T>, Optional<T>> reduction) {
        if(attempt.contains(":")) {
            return get(RegistryKey.parse(attempt));
        }
        return reduction.apply(getMatches(attempt));
    }
}
