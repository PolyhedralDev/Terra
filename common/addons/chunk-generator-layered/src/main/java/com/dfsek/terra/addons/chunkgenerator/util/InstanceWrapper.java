package com.dfsek.terra.addons.chunkgenerator.util;

import java.util.function.Supplier;


public record InstanceWrapper<T>(T instance) implements Supplier<T> {
    
    @Override
    public T get() {
        return instance;
    }
}
