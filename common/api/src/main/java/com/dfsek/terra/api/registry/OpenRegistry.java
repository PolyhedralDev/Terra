package com.dfsek.terra.api.registry;

import com.dfsek.terra.api.registry.exception.DuplicateEntryException;


public interface OpenRegistry<T> extends Registry<T> {
    /**
     * Add a value to this registry.
     *
     * @param identifier Identifier to assign value.
     * @param value      Value to register.
     */
    boolean register(String identifier, T value);
    
    /**
     * Add a value to this registry, checking whether it is present first.
     *
     * @param identifier Identifier to assign value.
     * @param value      Value to register.
     *
     * @throws DuplicateEntryException If an entry with the same identifier is already present.
     */
    void registerChecked(String identifier, T value) throws DuplicateEntryException;
    
    /**
     * Clears all entries from the registry.
     */
    void clear();
}
