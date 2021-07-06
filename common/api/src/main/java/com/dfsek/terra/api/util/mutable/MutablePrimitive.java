package com.dfsek.terra.api.util.mutable;

public interface MutablePrimitive<T> extends Comparable<T> {
    T get();

    void set(T value);
}
