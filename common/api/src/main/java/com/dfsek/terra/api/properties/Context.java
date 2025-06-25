/*
 * Copyright (c) 2020-2025 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.properties;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;


public class Context {
    private static final AtomicInteger size = new AtomicInteger(0);
    private static final Map<Class<? extends Properties>, PropertyKey<?>> properties = new HashMap<>();
    private final Map<Class<? extends Properties>, Properties> map = new HashMap<>();
    private Properties[] list = new Properties[size.get()];

    @SuppressWarnings("unchecked")
    public static <T extends Properties> PropertyKey<T> create(Class<T> clazz) {
        return (PropertyKey<T>) properties.computeIfAbsent(clazz, c -> new PropertyKey<>(size.getAndIncrement(), clazz));
    }

    @SuppressWarnings("unchecked")
    public <T extends Properties> T get(Class<T> clazz) {
        return (T) map.computeIfAbsent(clazz, k -> {
            throw new IllegalArgumentException("No properties registered for class " + clazz.getCanonicalName());
        });
    }

    public Context put(Properties properties) {
        if(map.containsKey(properties.getClass())) throw new IllegalArgumentException(
            "Property for class " + properties.getClass().getCanonicalName() + " already registered.");
        map.put(properties.getClass(), properties);
        return this;
    }

    public synchronized <T extends Properties> Context put(PropertyKey<T> key, T properties) {
        if(list.length != size.get()) {
            Properties[] p2 = new Properties[size.get()];
            System.arraycopy(list, 0, p2, 0, list.length);
            list = p2;
        }
        list[key.key] = properties;
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T extends Properties> T get(PropertyKey<T> key) {
        return (T) list[key.key];
    }

    public <T extends Properties> boolean has(Class<T> test) {
        return map.containsKey(test);
    }
}
