package com.dfsek.terra.registry;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class TerraRegistry<T> implements TypeLoader<T> {
    private final Map<String, T> objects = new HashMap<>();

    @Override
    public T load(Type type, Object o, ConfigLoader configLoader) throws LoadException {
        T obj = get((String) o);
        if(obj == null)
            throw new LoadException("No such " + type.getTypeName() + " matching \"" + o + "\" was found in this registry.");
        return obj;
    }

    /**
     * Add an object to the registry with a name.
     *
     * @param name  Name of the tree.
     * @param value Object to increment
     * @return True if tree was overwritten.
     */
    public boolean add(String name, T value) {
        boolean exists = objects.containsKey(name);
        objects.put(name, value);
        return exists;
    }

    /**
     * Check if the registry contains an object.
     *
     * @param name Name of the object.
     * @return Whether the registry contains the object.
     */
    public boolean contains(String name) {
        return objects.containsKey(name);
    }

    /**
     * Get an object from the registry,
     *
     * @param id ID of object to get
     * @return Object
     */
    public T get(String id) {
        return objects.get(id);
    }

    public void forEach(Consumer<T> consumer) {
        objects.forEach((id, obj) -> consumer.accept(obj));
    }

    public void forEach(BiConsumer<String, T> consumer) {
        objects.forEach(consumer);
    }

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
