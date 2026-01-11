/*
 * Copyright (c) 2020-2025 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.util.generic.data.types;

import com.dfsek.terra.api.util.generic.control.Monad;

import com.dfsek.terra.api.util.generic.data.BiFunctor;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import static com.dfsek.terra.api.util.function.FunctionUtils.*;


public sealed interface Either<L, R> extends Monad<R, Either<?, ?>>, BiFunctor<L, R, Either<?, ?>> {
    static <T> T collapse(Either<T, T> either) {
        return either.collect(identity(), identity());
    }

    @SuppressWarnings("unchecked")
    static <T, L> Either<L, T> toEither(Optional<T> o, L de) {
        return (Either<L, T>) o.map(Either::right).orElseGet(() -> Either.left(de));
    }

    @NotNull
    @Contract("_ -> this")
    default Either<L, R> ifLeft(@NotNull Consumer<L> action) {
        return mapLeft(lift(action));
    }

    @NotNull
    @Contract("_ -> this")
    default Either<L, R> ifRight(@NotNull Consumer<R> action) {
        return mapRight(lift(action));
    }

    // Either is a functor in its right parameter.
    @Override
    default <U> @NotNull Either<L, U> map(@NotNull Function<R, U> map) {
        return mapRight(map);
    }

    @Override
    default <T1> @NotNull Either<?, T1> pure(@NotNull T1 t) {
        return Either.right(t);
    }

    @Override
    <T2> @NotNull Either<L, T2> bind(@NotNull Function<R, Monad<T2, Either<?, ?>>> map);

    @Override
    <L1> @NotNull Either<L1, R> mapLeft(@NotNull Function<L, L1> f);

    @Override
    <R1> @NotNull Either<L, R1> mapRight(@NotNull Function<R, R1> f);

    @Override
    @NotNull
    default <V, W> Either<V, W> bimap(@NotNull Function<L, V> left, @NotNull Function<R, W> right) {
        return (Either<V, W>) BiFunctor.super.bimap(left, right);
    }

    default Maybe<L> getLeft() {
        return collapse(bimap(Maybe::just, lift(Maybe::nothing)));
    }

    default Maybe<R> getRight() {
        return collapse(bimap(lift(Maybe::nothing), Maybe::just));
    }

    default boolean isLeft() {
        return collapse(bimap(lift(true), lift(false)));
    }

    default boolean isRight() {
        return collapse(bimap(lift(false), lift(true)));
    }

    Either<R, L> flip();

    <U> U collect(Function<L, U> left, Function<R, U> right);

    default Either<L, R> consume(Consumer<L> left, Consumer<R> right) {
        return mapLeft(l -> {
            left.accept(l);
            return l;
        }).mapRight(r -> {
            right.accept(r);
            return r;
        });
    }

    @SuppressWarnings("Convert2MethodRef")
    default <T extends Throwable> R collectThrow(Function<L, T> left) throws T {
        return mapLeft(left).collect(l -> sneakyThrow(l), identity());
    }

    @NotNull
    @Contract("_ -> new")
    static <L1, R1> Either<L1, R1> left(L1 left) {
        return new Left<>(Objects.requireNonNull(left));
    }

    @NotNull
    @Contract("_ -> new")
    static <L1, R1> Either<L1, R1> right(R1 right) {
        return new Right<>(Objects.requireNonNull(right));
    }

    record Left<L, R>(L value) implements Either<L, R> {

        @Override
        @SuppressWarnings("unchecked")
        public <T2> @NotNull Either<L, T2> bind(@NotNull Function<R, Monad<T2, Either<?, ?>>> map) {
            return (Either<L, T2>) this;
        }

        @Override
        public <L1> @NotNull Either<L1, R> mapLeft(@NotNull Function<L, L1> f) {
            return new Left<>(f.apply(value));
        }

        @SuppressWarnings({ "unchecked" })
        @Override
        public <R1> @NotNull Either<L, R1> mapRight(@NotNull Function<R, R1> f) {
            return (Either<L, R1>) this;
        }

        @Override
        public Either<R, L> flip() {
            return Either.right(value);
        }

        @Override
        public <U> U collect(Function<L, U> left, Function<R, U> right) {
            return left.apply(value);
        }
    }


    record Right<L, R>(R value) implements Either<L, R> {
        @Override
        public <T2> @NotNull Either<L, T2> bind(@NotNull Function<R, Monad<T2, Either<?, ?>>> map) {
            return (Either<L, T2>) map.apply(value);
        }

        @SuppressWarnings({ "unchecked" })
        @Override
        public <L1> @NotNull Either<L1, R> mapLeft(@NotNull Function<L, L1> f) {
            return (Either<L1, R>) this;
        }

        @Override
        public <R1> @NotNull Either<L, R1> mapRight(@NotNull Function<R, R1> f) {
            return new Right<>(f.apply(value));
        }

        @Override
        public Either<R, L> flip() {
            return Either.left(value);
        }

        @Override
        public <U> U collect(Function<L, U> left, Function<R, U> right) {
            return right.apply(value);
        }
    }
}