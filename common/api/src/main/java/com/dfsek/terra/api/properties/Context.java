/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.properties;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class Context {
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    
    private final Map<Class<? extends Properties>, Properties> map = new HashMap<>();
    
    @SuppressWarnings("unchecked")
    public <T extends Properties> T get(Class<T> clazz) {
        rwLock.readLock().lock();
        try {
            final T object = (T) map.get(clazz);
            if (object == null) throw new IllegalArgumentException("No properties registered for class " + clazz.getCanonicalName());
            return object;
        } finally {
            rwLock.readLock().unlock();
        }
    }
    
    public Context put(Properties properties) {
        if(this.has(properties.getClass())) throw new IllegalArgumentException(
                "Property for class " + properties.getClass().getCanonicalName() + " already registered.");
        rwLock.writeLock().lock();
        try {
            map.put(properties.getClass(), properties);
        } finally {
            rwLock.writeLock().unlock();
        }
        return this;
    }
    
    public <T extends Properties> boolean has(Class<T> test) {
        rwLock.readLock().lock();
        try {
            return map.containsKey(test);
        } finally {
            rwLock.readLock().unlock();
        }
    }
}
