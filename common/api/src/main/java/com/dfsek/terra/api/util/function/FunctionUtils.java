package com.dfsek.terra.api.util.function;

import com.dfsek.terra.api.util.generic.data.types.Either;

import com.dfsek.terra.api.util.generic.data.types.Maybe;
import com.dfsek.terra.api.util.generic.data.types.Pair;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;


public final class FunctionUtils {
    private FunctionUtils() { }

    public static <T> Function<T, T> identity() {
        return Function.identity();
    }

    public static <T> Maybe<T> just(T t) {
        return Maybe.just(t);
    }

    public static <T> Maybe<T> nothing() {
        return Maybe.nothing();
    }

    public static <L, R> Either<L, R> left(L l) {
        return Either.left(l);
    }

    public static <L, R> Either<L, R> right(R r) {
        return Either.right(r);
    }

    public static <T> Maybe<T> fromOptional(Optional<T> op) {
        return Maybe.fromOptional(op);
    }

    @Contract("_ -> new")
    public static <T> @NotNull Function<T, T> lift(@NotNull Consumer<T> c) {
        Objects.requireNonNull(c);
        return co -> {
            c.accept(co);
            return co;
        };
    }

    @Contract("_ -> new")
    public static <T, R> @NotNull Function<T, R> lift(@NotNull Supplier<R> c) {
        Objects.requireNonNull(c);
        return co -> c.get();
    }

    @Contract("_ -> new")
    public static <T, R> @NotNull Function<T, R> lift(@NotNull R c) {
        return lift(() -> c);
    }

    @Contract("_ -> fail")
    public static <T extends Throwable, U> @NotNull U throw_(@NotNull T e) throws T {
        throw e;
    }

    @SuppressWarnings("unchecked")
    @Contract("_ -> fail")
    public static <E extends Throwable, U> @NotNull U sneakyThrow(@NotNull Throwable e) throws E {
        throw (E) e;
    }

    @Contract(pure = true, value = "_ -> new")
    public static <T, U> @NotNull Function<T, Either<Exception, U>> liftTry(@NotNull Function<T, U> f) {
        return s -> {
            try {
                return Either.right(f.apply(s));
            } catch(Exception e) {
                return Either.left(e);
            }
        };
    }

    @Contract(pure = true, value = "_ -> new")
    public static <T, U> @NotNull Function<T, Either<Throwable, U>> liftTryUnsafe(@NotNull Function<T, U> f) {
        return s -> {
            try {
                return Either.right(f.apply(s));
            } catch(Throwable e) {
                return Either.left(e);
            }
        };
    }

    @Contract(pure = true, value = "_ -> new")
    public static <T, U, R> @NotNull Function<Pair<T, U>, R> tuple(@NotNull BiFunction<T, U, R> f) {
        return p -> f.apply(p.left(), p.right());
    }

    @Contract(pure = true, value = "_ -> new")
    public static <T, U, R> @NotNull BiFunction<T, U, R> untuple(@NotNull Function<Pair<T, U>, R> f) {
        return (a, b) -> f.apply(Pair.of(a, b));
    }

    @Contract(pure = true, value = "_ -> new")
    public static <T, U, R> @NotNull Function<T, Function<U, R>> curry(@NotNull BiFunction<T, U, R> f) {
        return a -> b -> f.apply(a, b);
    }

    @Contract(pure = true, value = "_ -> new")
    public static <T, U, R> @NotNull BiFunction<T, U, R> uncurry(@NotNull Function<T, Function<U, R>> f) {
        return (a, b) -> f.apply(a).apply(b);
    }

    @Contract(pure = true, value = "_ -> new")
    public static <T> @NotNull T construct(@NotNull Supplier<T> in) {
        return Objects.requireNonNull(Objects.requireNonNull(in).get());
    }
}
