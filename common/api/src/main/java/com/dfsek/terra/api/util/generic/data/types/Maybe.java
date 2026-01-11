package com.dfsek.terra.api.util.generic.data.types;

import com.dfsek.terra.api.util.generic.control.Monad;
import com.dfsek.terra.api.util.generic.data.LinkedList;

import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;


public sealed interface Maybe<T> extends Monad<T, Maybe<?>> {
    @Override
    default <T1> @NotNull Maybe<T1> pure(@NotNull T1 t) {
        return just(t);
    }

    @Override
    <T2> @NotNull Maybe<T2> bind(@NotNull Function<T, Monad<T2, Maybe<?>>> map);

    Optional<T> toOptional();

    <L> Either<L, T> toEither(L l);

    T get(Supplier<T> def);

    boolean isJust();

    @Override
    <U> @NotNull Maybe<U> map(@NotNull Function<T, U> map);

    default T get(T def) {
        return get(() -> def);
    }

    default Maybe<T> consume(Consumer<T> c) {
        return map(m -> {
            c.accept(m);
            return m;
        });
    }

    default Maybe<T> ifNothing(Runnable r) {
        if(!isJust()) {
            r.run();
        }
        return this;
    }

    default Maybe<T> collapse(Runnable nothing, Consumer<T> just) {
        return consume(just).ifNothing(nothing);
    }


    default LinkedList<T> toList() {
        return map(LinkedList::of).get(LinkedList::empty);
    }

    /**
     * Project a new value into this Maybe if it is Nothing.
     * Effectively a bind operation over Nothing, treating it as a Void type.
     * <p>
     * Converse of {@link #bind}.
     */
    Maybe<T> or(Supplier<Maybe<T>> or);

    default Maybe<T> orJust(Supplier<T> or) {
        return or(() -> just(or.get()));
    }

    @Deprecated
    default <X extends Throwable> T orThrow() {
        return get(() -> { throw new NoSuchElementException("No value present."); });
    }

    @Deprecated
    default <X extends Throwable> T orThrow(Supplier<X> e) throws X {
        if(isJust()) {
            return orThrow();
        }
        throw e.get();
    }

    default Maybe<T> or(Maybe<T> or) {
        return or(() -> or);
    }

    default Stream<T> toStream() {
        return map(Stream::of).get(Stream.empty());
    }

    default Maybe<T> filter(Predicate<T> filter) {
        return bind(o -> filter.test(o) ? this : nothing());
    }

    static <T> Maybe<T> fromOptional(Optional<T> op) {
        return op.map(Maybe::just).orElseGet(Maybe::nothing);
    }

    static <T> Maybe<T> ofNullable(T t) {
        if(t == null) return nothing();
        return just(t);
    }

    static <T1> Maybe<T1> just(T1 t) {

        return new Just<>(t);
    }

    static <T1> Maybe<T1> nothing() {

        return new Nothing<>();
    }

    record Just<T>(T value) implements Maybe<T> {
        @Override
        public Optional<T> toOptional() {
            return Optional.of(value);
        }

        @Override
        public <L> Either<L, T> toEither(L l) {
            return Either.right(value);
        }

        @Override
        public T get(Supplier<T> def) {
            return value;
        }

        @Override
        public boolean isJust() {
            return true;
        }

        @Override
        public <U> @NotNull Maybe<U> map(@NotNull Function<T, U> map) {
            return just(map.apply(value));
        }

        @Override
        public Maybe<T> or(Supplier<Maybe<T>> or) {
            return this;
        }

        @Override
        public <T2> @NotNull Maybe<T2> bind(@NotNull Function<T, Monad<T2, Maybe<?>>> map) {
            return (Maybe<T2>) map.apply(value);
        }
    }


    record Nothing<T>() implements Maybe<T> {
        @Override
        public <T2> @NotNull Maybe<T2> bind(@NotNull Function<T, Monad<T2, Maybe<?>>> map) {
            return nothing();
        }

        @Override
        public Optional<T> toOptional() {
            return Optional.empty();
        }

        @Override
        public <L> Either<L, T> toEither(L l) {
            return Either.left(l);
        }

        @Override
        public T get(Supplier<T> def) {
            return def.get();
        }

        @Override
        public boolean isJust() {
            return false;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <U> @NotNull Maybe<U> map(@NotNull Function<T, U> map) {
            return (Maybe<U>) this;
        }

        @Override
        public Maybe<T> or(Supplier<Maybe<T>> or) {
            return or.get();
        }
    }
}
