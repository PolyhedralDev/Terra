package com.dfsek.terra.registry;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.terra.registry.exception.DuplicateEntryException;

import java.lang.reflect.Type;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Wrapper for a registry that ensures checked access.
 *
 * @param <T> Type in registry
 */
public class CheckedRegistry<T> implements TypeLoader<T> {
    private final TerraRegistry<T> registry;

    public CheckedRegistry(TerraRegistry<T> registry) {
        this.registry = registry;
    }

    /**
     * Add a value to this registry, checking whether it is present first.
     *
     * @param identifier Identifier to assign value.
     * @param value      Value to add.
     * @throws DuplicateEntryException If an entry with the same identifier is already present.
     */
    public void add(String identifier, T value) throws DuplicateEntryException {
        registry.addChecked(identifier, value);
    }

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
    public void addUnchecked(String identifier, T value) {
        registry.add(identifier, value);
    }

    /**
     * Get a value from the registry.
     *
     * @param identifier Identifier of value.
     * @return Value matching the identifier, {@code null} if no value is present.
     */
    public T get(String identifier) {
        return registry.get(identifier);
    }

    /**
     * Check if the registry contains a value.
     *
     * @param identifier Identifier of value.
     * @return Whether the registry contains the value.
     */
    public boolean contains(String identifier) {
        return registry.contains(identifier);
    }

    /**
     * Perform the given action for every value in the registry.
     *
     * @param consumer Action to perform on value.
     */
    public void forEach(Consumer<T> consumer) {
        registry.forEach(consumer);
    }

    /**
     * Perform an action for every key-value pair in the registry.
     *
     * @param consumer Action to perform on pair.
     */
    public void forEach(BiConsumer<String, T> consumer) {
        registry.forEach(consumer);
    }

    /**
     * Get the entries of this registry as a {@link Set}.
     *
     * @return Set containing all entries.
     */
    public Set<T> entries() {
        return registry.entries();
    }

    @Override
    public T load(Type t, Object c, ConfigLoader loader) throws LoadException {
        return registry.load(t, c, loader);
    }
}
