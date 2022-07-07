/*
 * Copyright (c) 2020-2021 Polyhedral Development
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
    private final AtomicReference<Properties[]> list = new AtomicReference<>(new Properties[size.get()]);
    
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
    
    public <T extends Properties> Context put(PropertyKey<T> key, T properties) {
        list.updateAndGet(p -> {
            if(p.length == size.get()) return p;
            Properties[] p2 = new Properties[size.get()];
            System.arraycopy(p, 0, p2, 0, p.length);
            return p2;
        })[key.key] = properties;
        return this;
    }
    
    @SuppressWarnings("unchecked")
    public <T extends Properties> T get(PropertyKey<T> key) {
        return (T) list.get()[key.key];
    }
    
    public <T extends Properties> boolean has(Class<T> test) {
        return map.containsKey(test);
    }
}
