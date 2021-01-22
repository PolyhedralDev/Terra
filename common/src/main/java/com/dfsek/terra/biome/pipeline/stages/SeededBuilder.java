package com.dfsek.terra.biome.pipeline.stages;

import java.util.function.Function;

public class SeededBuilder<T> {
    private final Function<Long, T> builder;

    public SeededBuilder(Function<Long, T> builder) {
        this.builder = builder;
    }

    public T build(long seed) {
        return builder.apply(seed);
    }
}
