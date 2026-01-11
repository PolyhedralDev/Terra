package com.dfsek.terra.api.util.generic.data;

import com.dfsek.terra.api.util.generic.data.types.Pair;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;


public interface BiFunctor<T, U, B extends BiFunctor<?, ?, B>> {
    @Contract("_ -> new")
    static <L, R, B extends BiFunctor<?, ?, B>> @NotNull Consumer<BiFunctor<L, R, B>> consumeLeft(@NotNull Consumer<L> consumer) {
        Objects.requireNonNull(consumer);
        return pair -> pair.mapLeft(p -> {
            consumer.accept(p);
            return p;
        });
    }

    @Contract("_ -> new")
    static <L, R, B extends BiFunctor<?, ?, B>> @NotNull Consumer<BiFunctor<L, R, B>> consumeRight(@NotNull Consumer<R> consumer) {
        Objects.requireNonNull(consumer);
        return pair -> pair.mapRight(p -> {
            consumer.accept(p);
            return p;
        });
    }

    @Contract(pure = true, value = "_ -> new")
    <V> @NotNull BiFunctor<V, U, B> mapLeft(@NotNull Function<T, V> map);

    @Contract(pure = true, value = "_ -> new")
    <V> @NotNull BiFunctor<T, V, B> mapRight(@NotNull Function<U, V> map);

    @Contract(pure = true, value = "_, _-> new")
    default <V, W> @NotNull BiFunctor<V, W, B> bimap(@NotNull Function<T, V> left, @NotNull Function<U, W> right) {
        return mapLeft(left).mapRight(right);
    }
}
