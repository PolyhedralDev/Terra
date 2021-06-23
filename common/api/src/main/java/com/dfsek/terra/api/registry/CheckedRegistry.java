package com.dfsek.terra.api.registry;

public interface CheckedRegistry<T> extends Registry<T> {
    /**
     * Add a value to this registry, checking whether it is present first.
     *
     * @param identifier Identifier to assign value.
     * @param value      Value to add.
     * @throws DuplicateEntryException If an entry with the same identifier is already present.
     */
    void add(String identifier, T value) throws DuplicateEntryException;

    /**
     * Add a value to the registry, without checking presence beforehand.
     * <p>
     * Use of this method is generally discouraged, as it is bad practice to overwrite registry values.
     *
     * @param identifier Identifier to assign value.
     * @param value      Value to add.
     * @deprecated Use of {@link #add(String, Object)} is encouraged.
     */
    @Deprecated
    void addUnchecked(String identifier, T value);
}
