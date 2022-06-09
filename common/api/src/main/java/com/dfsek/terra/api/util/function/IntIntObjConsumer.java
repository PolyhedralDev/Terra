package com.dfsek.terra.api.util.function;

@FunctionalInterface
public interface IntIntObjConsumer<T> {
    void accept(int i, int j, T obj);
}
