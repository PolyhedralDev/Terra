package com.dfsek.terra.api.registry;

import com.dfsek.tectonic.loading.TypeLoader;

import java.util.Collection;
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
    T get(String identifier);
    
    /**
     * Check if the registry contains a value.
     *
     * @param identifier Identifier of value.
     *
     * @return Whether the registry contains the value.
     */
    boolean contains(String identifier);
    
    /**
     * Perform the given action for every value in the registry.
     *
     * @param consumer Action to perform on value.
     */
    void forEach(Consumer<T> consumer);
    
    /**
     * Perform an action for every key-value pair in the registry.
     *
     * @param consumer Action to perform on pair.
     */
    void forEach(BiConsumer<String, T> consumer);
    
    /**
     * Get the entries of this registry as a {@link Set}.
     *
     * @return Set containing all entries.
     */
    Collection<T> entries();
    
    /**
     * Get all the keys in this registry.
     *
     * @return Keys in registry
     */
    Set<String> keys();
}
