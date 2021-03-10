package com.dfsek.terra.api.util.generic.pair;

public class ImmutablePair<L, R> {
    private final L left;
    private final R right;

    public ImmutablePair(L left, R right) {
        this.left = left;
        this.right = right;
    }

    public static <L1, R1> ImmutablePair<L1, R1> of(L1 left, R1 right) {
        return new ImmutablePair<>(left, right);
    }

    public R getRight() {
        return right;
    }

    public L getLeft() {
        return left;
    }

    public Pair<L, R> mutable() {
        return new Pair<>(left, right);
    }
}
