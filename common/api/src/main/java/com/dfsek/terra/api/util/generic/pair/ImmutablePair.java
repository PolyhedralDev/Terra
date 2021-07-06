package com.dfsek.terra.api.util.generic.pair;

public final class ImmutablePair<L, R> {
    private final L left;
    private final R right;

    private static final ImmutablePair<?, ?> NULL = new ImmutablePair<>(null, null);

    private ImmutablePair(L left, R right) {
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

    @SuppressWarnings("unchecked")
    public static <L1, R1> ImmutablePair<L1, R1> ofNull() {
        return (ImmutablePair<L1, R1>) NULL;
    }

    public Pair<L, R> mutable() {
        return Pair.of(left, right);
    }
}
