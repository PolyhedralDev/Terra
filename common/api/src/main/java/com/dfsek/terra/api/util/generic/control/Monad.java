package com.dfsek.terra.api.util.generic.control;

import com.dfsek.terra.api.util.generic.data.Functor;
import com.dfsek.terra.api.util.generic.kinds.K;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Function;


/**
 * A monad is a monoid in the category of endofunctors.
 */
public interface Monad<T, M extends Monad<?, M>> extends Functor<T, M>, K<M, T> {
    @Contract(pure = true, value = "_ -> new")
    <T2> @NotNull Monad<T2, M> bind(@NotNull Function<T, Monad<T2, M>> map);

    @Contract(pure = true, value = "_ -> new")
    <T1> @NotNull Monad<T1, M> pure(@NotNull T1 t);

    @Override
    @Contract(pure = true, value = "_ -> new")
    default <U> @NotNull Monad<U, M> map(@NotNull Function<T, U> map) {
        return bind(Objects.requireNonNull(map).andThen(this::pure));
    }

    // almost all well-known applicative functors are also monads, so we can just put that here.
    @Contract(pure = true, value = "_ -> new")
    default <U> @NotNull Monad<U, M> apply(@NotNull Monad<Function<T, U>, M> amap) {
        return Objects.requireNonNull(amap).bind(this::map);
    }
}
