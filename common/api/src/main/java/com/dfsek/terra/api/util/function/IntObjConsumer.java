package com.dfsek.terra.api.util.function;

@FunctionalInterface
public interface IntObjConsumer<T> {
    void accept(int i, T obj);
}
