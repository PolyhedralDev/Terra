package com.dfsek.terra.api.util.function.monad;

import java.util.function.Function;


public interface Monad<T> {
    <U> Monad<U> bind(Function<T, Monad<U>> map);
}
