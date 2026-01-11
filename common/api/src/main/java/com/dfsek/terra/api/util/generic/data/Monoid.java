package com.dfsek.terra.api.util.generic.data;

import com.dfsek.terra.api.util.generic.kinds.K;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;


public interface Monoid<T, M extends Monoid<?, M>> extends K<M, T> {
    @Contract(pure = true, value = "-> new")
    <T1> @NotNull Monoid<T1, M> identity();

    @Contract(pure = true, value = "_ -> new")
    @NotNull Monoid<T, M> multiply(@NotNull Monoid<T, M> t);
}
