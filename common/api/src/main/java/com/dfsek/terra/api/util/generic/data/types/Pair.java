/*
 * Copyright (c) 2020-2025 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.util.generic.data.types;

import com.dfsek.terra.api.util.generic.data.BiFunctor;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;


public record Pair<L, R>(L left, R right) implements BiFunctor<L, R, Pair<?, ?>> {
    private static final Pair<?, ?> NULL = new Pair<>(null, null);

    public <T> @NotNull Pair<T, R> mapLeft(@NotNull Function<L, T> function) {
        return of(function.apply(left), right);
    }

    public <T> @NotNull Pair<L, T> mapRight(@NotNull Function<R, T> function) {
        return of(left, function.apply(right));
    }

    public static <L, R> Predicate<Pair<L, R>> testLeft(Predicate<L> predicate) {
        return pair -> predicate.test(pair.left);
    }

    public static <R> Predicate<Pair<?, R>> testRight(Predicate<R> predicate) {
        return pair -> predicate.test(pair.right);
    }

    @Contract("_, _ -> new")
    public static <L1, R1> Pair<L1, R1> of(L1 left, R1 right) {
        return new Pair<>(left, right);
    }

    @Contract("-> new")
    @SuppressWarnings("unchecked")
    public static <L1, R1> Pair<L1, R1> ofNull() {
        return (Pair<L1, R1>) NULL;
    }

    @NotNull
    @Contract("-> new")
    public Pair.Mutable<L, R> mutable() {
        return Mutable.of(left, right);
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Pair<?, ?>(Object left1, Object right1))) return false;

        return Objects.equals(this.left, left1) && Objects.equals(this.right, right1);
    }

    public Pair<L, R> apply(BiConsumer<L, R> consumer) {
        consumer.accept(this.left, this.right);
        return this;
    }

    @Override
    public String toString() {
        return String.format("{%s,%s}", left, right);
    }


    public static class Mutable<L, R> {
        private L left;
        private R right;

        private Mutable(L left, R right) {
            this.left = left;
            this.right = right;
        }

        @NotNull
        @Contract("_, _ -> new")
        public static <L1, R1> Pair.Mutable<L1, R1> of(L1 left, R1 right) {
            return new Mutable<>(left, right);
        }

        @Contract("-> new")
        public Pair<L, R> immutable() {
            return Pair.of(left, right);
        }

        public L left() {
            return left;
        }

        public void setLeft(L left) {
            this.left = left;
        }

        public R right() {
            return right;
        }

        public void setRight(R right) {
            this.right = right;
        }

        @Override
        public int hashCode() {
            return Objects.hash(left, right);
        }

        @Override
        public boolean equals(Object obj) {
            if(!(obj instanceof Mutable<?, ?> that)) return false;

            return Objects.equals(this.left, that.left) && Objects.equals(this.right, that.right);
        }
    }
}
