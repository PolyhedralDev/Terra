package com.dfsek.terra.api.util.seeded;

import java.util.function.Function;

@FunctionalInterface
public interface SeededBuilder<T> extends Function<Long, T> {

}
