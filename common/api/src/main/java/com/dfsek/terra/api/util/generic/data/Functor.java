package com.dfsek.terra.api.util.generic.data;

import com.dfsek.terra.api.util.generic.kinds.K;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;


public interface Functor<T, F extends Functor<?, F>> extends K<F, T> {
    @Contract(pure = true, value = "_ -> new")
    <U> @NotNull Functor<U, F> map(@NotNull Function<T, U> map);
}
