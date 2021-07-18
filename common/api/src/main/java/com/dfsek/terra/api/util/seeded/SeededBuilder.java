package com.dfsek.terra.api.util.seeded;

@FunctionalInterface
public interface SeededBuilder<T> {
    T build(long seed);
}
