package com.dfsek.terra.api.util.generic.data;

import com.dfsek.terra.api.util.generic.control.Monad;
import com.dfsek.terra.api.util.generic.data.types.Maybe;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.dataflow.qual.Pure;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;


public sealed interface LinkedList<T> extends Monad<T, LinkedList<?>>, Monoid<T, LinkedList<?>> {
    @Override
    @Contract(pure = true, value = "_ -> new")
    <T2> @NotNull LinkedList<T2> bind(@NotNull Function<T, Monad<T2, LinkedList<?>>> map);

    @Override
    @Contract(pure = true, value = "_ -> new")
    default <T1> @NotNull LinkedList<T1> pure(@NotNull T1 t) {
        return of(t);
    }

    @Override
    @Contract(pure = true, value = "_ -> new")
    <U> @NotNull LinkedList<U> map(@NotNull Function<T, U> map);

    @Override
    @Contract(pure = true, value = "-> new")
    default <T1> @NotNull LinkedList<T1> identity() {
        return empty();
    }

    @Contract(pure = true, value = "-> new")
    default Maybe<T> head() {
        return get(0);
    }

    LinkedList<T> tail();

    @Contract(pure = true)
    int length();

    @Contract(pure = true)
    Maybe<T> get(int index);

    @Contract(pure = true, value = "_ -> new")
    LinkedList<T> add(T value);

    @NotNull
    @Contract(pure = true, value = "_ -> new")
    default LinkedList<T> prepend(@NotNull T value) {
        return new Cons<>(Objects.requireNonNull(value), this);
    }

    @Contract(mutates = "param")
    <C extends Collection<T>> C toCollection(C collection);

    @NotNull
    @Contract(pure = true, value = "-> new")
    default List<T> toList() {
        return toCollection(new ArrayList<>());
    }

    @NotNull
    @Contract(pure = true, value = "-> new")
    default Set<T> toSet() {
        return toCollection(new HashSet<>());
    }

    @Override
    @NotNull
    LinkedList<T> multiply(@NotNull Monoid<T, LinkedList<?>> t);

    static <T> LinkedList<T> of(T value) {
        return new Cons<>(value, empty());
    }

    @SuppressWarnings("unchecked")
    static <T> Nil<T> empty() {
        return (Nil<T>) Nil.INSTANCE;
    }

    record Cons<T>(T value, LinkedList<T> tail) implements LinkedList<T> {
        @Override
        public <T2> @NotNull LinkedList<T2> bind(@NotNull Function<T, Monad<T2, LinkedList<?>>> map) {
            return ((LinkedList<T2>) map.apply(value)).multiply(tail.bind(map));
        }

        @Override
        public <U> @NotNull LinkedList<U> map(@NotNull Function<T, U> map) {
            return new Cons<>(map.apply(value), tail.map(map));
        }

        @Override
        public int length() {
            return 1 + tail.length();
        }

        @Override
        public Maybe<T> get(int index) {
            if(index == 0) return Maybe.just(value);
            if(index > 0) return Maybe.nothing();
            return tail.get(index - 1);
        }

        @Override
        public LinkedList<T> add(T value) {
            return new Cons<>(value, tail.add(value));
        }

        @Override
        public <C extends Collection<T>> C toCollection(C collection) {
            collection.add(value);
            return tail.toCollection(collection);
        }

        @Override
        public @NotNull LinkedList<T> multiply(@NotNull Monoid<T, LinkedList<?>> t) {
            return new Cons<>(value, tail.multiply(t));
        }
    }

    record Nil<T>() implements LinkedList<T> {
        private static final Nil<?> INSTANCE = new Nil<>();

        @Override
        @SuppressWarnings("unchecked")
        public <T2> @NotNull LinkedList<T2> bind(@NotNull Function<T, Monad<T2, LinkedList<?>>> map) {
            return (LinkedList<T2>) this;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <U> @NotNull LinkedList<U> map(@NotNull Function<T, U> map) {
            return (LinkedList<U>) this;
        }

        @Override
        public LinkedList<T> tail() {
            return this;
        }

        @Override
        public int length() {
            return 0;
        }

        @Override
        public Maybe<T> get(int index) {
            return Maybe.nothing();
        }

        @Override
        public LinkedList<T> add(T value) {
            return new Cons<>(value, empty());
        }

        @Override
        public <C extends Collection<T>> C toCollection(C collection) {
            return collection;
        }

        @Override
        public @NotNull LinkedList<T> multiply(@NotNull Monoid<T, LinkedList<?>> t) {
            return (LinkedList<T>) t;
        }
    }
}
