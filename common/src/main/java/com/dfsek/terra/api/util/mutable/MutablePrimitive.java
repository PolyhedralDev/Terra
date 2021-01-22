package com.dfsek.terra.api.util.mutable;

public interface MutablePrimitive<T> {
    T get();

    void set(T value);
}
