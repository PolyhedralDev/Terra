/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.util.generic.either;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;


public final class Either<L, R> {
    private final L left;
    private final R right;
    private final boolean leftPresent;
    
    private Either(L left, R right, boolean leftPresent) {
        this.left = left;
        this.right = right;
        this.leftPresent = leftPresent;
    }
    
    @NotNull
    @Contract("_ -> new")
    public static <L1, R1> Either<L1, R1> left(L1 left) {
        return new Either<>(Objects.requireNonNull(left), null, true);
    }
    
    @NotNull
    @Contract("_ -> new")
    public static <L1, R1> Either<L1, R1> right(R1 right) {
        return new Either<>(null, Objects.requireNonNull(right), false);
    }
    
    @NotNull
    @Contract("_ -> this")
    public Either<L, R> ifLeft(Consumer<L> action) {
        if(leftPresent) action.accept(left);
        return this;
    }
    
    @NotNull
    @Contract("_ -> this")
    public Either<L, R> ifRight(Consumer<R> action) {
        if(!leftPresent) action.accept(right);
        return this;
    }
    
    @NotNull
    public Optional<L> getLeft() {
        if(leftPresent) return Optional.of(left);
        return Optional.empty();
    }
    
    @NotNull
    public Optional<R> getRight() {
        if(!leftPresent) return Optional.of(right);
        return Optional.empty();
    }
    
    public boolean hasLeft() {
        return leftPresent;
    }
    
    public boolean hasRight() {
        return !leftPresent;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }
    
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Either<?, ?> that)) return false;

        return (this.leftPresent && that.leftPresent && Objects.equals(this.left, that.left))
               || (!this.leftPresent && !that.leftPresent && Objects.equals(this.right, that.right));
    }
}
