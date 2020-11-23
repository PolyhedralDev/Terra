package com.dfsek.terra.registry;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.util.Map;

public abstract class TerraRegistry<T> {
    private final Map<String, T> objects = new Object2ObjectOpenHashMap<>();

    /**
     * Add an object to the registry with a name.
     *
     * @param name  Name of the tree.
     * @param value Object to add
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
}
