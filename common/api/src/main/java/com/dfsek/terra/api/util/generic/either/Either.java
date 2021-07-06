package com.dfsek.terra.api.util.generic.either;

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

    public static <L1, R1> Either<L1, R1> left(L1 left) {
        return new Either<>(left, null, true);
    }

    public static <L1, R1> Either<L1, R1> right(R1 right) {
        return new Either<>(null, right, false);
    }

    public Optional<L> getLeft() {
        if(leftPresent) return Optional.of(left);
        return Optional.empty();
    }

    public Optional<R> getRight() {
        if(!leftPresent) return Optional.of(right);
        return Optional.empty();
    }

    public Either<L, R> ifLeft(Consumer<L> action) {
        if(leftPresent) action.accept(left);
        return this;
    }

    public Either<L, R> ifRight(Consumer<R> action) {
        if(!leftPresent) action.accept(right);
        return this;
    }

    public boolean hasLeft() {
        return leftPresent;
    }

    public boolean hasRight() {
        return !leftPresent;
    }
}
