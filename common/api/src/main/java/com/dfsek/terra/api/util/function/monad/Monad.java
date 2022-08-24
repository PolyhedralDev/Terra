package com.dfsek.terra.api.util.function.monad;

import java.util.function.Function;

import com.dfsek.terra.api.util.function.functor.Functor;


public interface Monad<T, M extends Monad<?, M>> extends Functor<T, M> {
    <U> Monad<U, M> bind(Function<T, Monad<U, M>> map);
    
    default <U> Monad<U, M> map(Function<T, U> fn) {
        return bind(a -> pure(fn.apply(a)));
    }
    
    <U> Monad<U, M> pure(U u);
}
