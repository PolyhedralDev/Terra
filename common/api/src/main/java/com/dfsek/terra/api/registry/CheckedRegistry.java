package com.dfsek.terra.api.registry;

import com.dfsek.terra.api.registry.exception.DuplicateEntryException;

public interface CheckedRegistry<T> extends Registry<T> {
    /**
     * Add a value to this registry, checking whether it is present first.
     *
     * @param identifier Identifier to assign value.
     * @param value      Value to register.
     * @throws DuplicateEntryException If an entry with the same identifier is already present.
     */
    void register(String identifier, T value) throws DuplicateEntryException;

    /**
     * Add a value to the registry, without checking presence beforehand.
     * <p>
     * Use of this method is generally discouraged, as it is bad practice to overwrite registry values.
     *
     * @param identifier Identifier to assign value.
     * @param value      Value to register.
     * @deprecated Use of {@link #register(String, Object)} is encouraged.
     */
    @Deprecated
    void addUnchecked(String identifier, T value);
}
