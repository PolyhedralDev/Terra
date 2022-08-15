package com.dfsek.terra.api.util.function.functor;

import java.util.function.Function;


public interface Functor<T, F extends Functor<?, F>> {
    <U> Functor<U, F> map(Function<T, U> map);
}
