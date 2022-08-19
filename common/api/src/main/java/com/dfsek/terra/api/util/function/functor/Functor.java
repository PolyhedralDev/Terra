package com.dfsek.terra.api.util.function.functor;

import java.util.function.Function;


public interface Functor<T> {
    <U> Functor<U> map(Function<T, U> map);
}
