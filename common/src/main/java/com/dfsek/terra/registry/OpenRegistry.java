package com.dfsek.terra.registry;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.terra.api.registry.Registry;
import com.dfsek.terra.registry.exception.DuplicateEntryException;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Registry implementation with read/write access. For internal use only.
 * @param <T>
 */
public abstract class OpenRegistry<T> implements Registry<T> {
    private final Map<String, T> objects = new HashMap<>();

    @Override
    public T load(Type type, Object o, ConfigLoader configLoader) throws LoadException {
        T obj = get((String) o);
        if(obj == null)
            throw new LoadException("No such " + type.getTypeName() + " matching \"" + o + "\" was found in this registry.");
        return obj;
    }

    /**
     * Add a value to this registry.
     *
     * @param identifier Identifier to assign value.
     * @param value      Value to add.
     */
    public boolean add(String identifier, T value) {
        boolean exists = objects.containsKey(identifier);
        objects.put(identifier, value);
        return exists;
    }

    /**
     * Add a value to this registry, checking whether it is present first.
     *
     * @param identifier Identifier to assign value.
     * @param value      Value to add.
     * @throws DuplicateEntryException If an entry with the same identifier is already present.
     */
    public void addChecked(String identifier, T value) throws DuplicateEntryException {
        if(objects.containsKey(identifier))
            throw new DuplicateEntryException("Value with identifier \"" + identifier + "\" is already defined in registry.");
        add(identifier, value);
    }

    @Override
    public boolean contains(String identifier) {
        return objects.containsKey(identifier);
    }

    @Override
    public T get(String identifier) {
        return objects.get(identifier);
    }

    @Override
    public void forEach(Consumer<T> consumer) {
        objects.forEach((id, obj) -> consumer.accept(obj));
    }

    @Override
    public void forEach(BiConsumer<String, T> consumer) {
        objects.forEach(consumer);
    }

    @Override
    public Set<T> entries() {
        return new HashSet<>(objects.values());
    }

    /**
     * Clears all entries from the registry.
     */
    public void clear() {
        objects.clear();
    }
}
