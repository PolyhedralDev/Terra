package com.dfsek.terra.api.registry;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.terra.registry.OpenRegistry;
import com.dfsek.terra.registry.exception.DuplicateEntryException;

import java.lang.reflect.Type;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Wrapper for a registry that ensures checked additions.
 *
 * @param <T> Type in registry
 */
public class CheckedRegistry<T> implements Registry<T> {
    private final OpenRegistry<T> registry;

    public CheckedRegistry(OpenRegistry<T> registry) {
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

    @Override
    public T get(String identifier) {
        return registry.get(identifier);
    }

    @Override
    public boolean contains(String identifier) {
        return registry.contains(identifier);
    }

    @Override
    public void forEach(Consumer<T> consumer) {
        registry.forEach(consumer);
    }

    @Override
    public void forEach(BiConsumer<String, T> consumer) {
        registry.forEach(consumer);
    }

    @Override
    public Set<T> entries() {
        return registry.entries();
    }

    @Override
    public T load(Type t, Object c, ConfigLoader loader) throws LoadException {
        return registry.load(t, c, loader);
    }
}
