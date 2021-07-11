package com.dfsek.terra.api.util.generic.pair;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class ImmutablePair<L, R> {
    private final L left;
    private final R right;

    private static final ImmutablePair<?, ?> NULL = new ImmutablePair<>(null, null);

    private ImmutablePair(L left, R right) {
        this.left = left;
        this.right = right;
    }

    @Contract("_, _ -> new")
    public static <L1, R1> ImmutablePair<L1, R1> of(L1 left, R1 right) {
        return new ImmutablePair<>(left, right);
    }

    public R getRight() {
        return right;
    }

    public L getLeft() {
        return left;
    }

    @Contract("-> new")
    @SuppressWarnings("unchecked")
    public static <L1, R1> ImmutablePair<L1, R1> ofNull() {
        return (ImmutablePair<L1, R1>) NULL;
    }

    @NotNull
    @Contract("-> new")
    public Pair<L, R> mutable() {
        return Pair.of(left, right);
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof ImmutablePair)) return false;

        ImmutablePair<?, ?> that = (ImmutablePair<?, ?>) obj;
        return Objects.equals(this.left, that.left) && Objects.equals(this.right, that.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }
}
