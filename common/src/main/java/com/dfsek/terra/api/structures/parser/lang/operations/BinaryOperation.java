package com.dfsek.terra.api.structures.parser.lang.operations;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.structures.parser.lang.Returnable;
import com.dfsek.terra.api.structures.tokenizer.Position;

public abstract class BinaryOperation<T> implements Returnable<T> {
    private final Returnable<T> left;
    private final Returnable<T> right;
    private final Position start;

    protected BinaryOperation(Returnable<T> left, Returnable<T> right, Position start) {
        this.left = left;
        this.right = right;
        this.start = start;
    }

    public abstract T apply(T left, T right);

    @Override
    public Position getPosition() {
        return getPosition();
    }

    @Override
    public T apply(Location location) {
        return apply(left.apply(location), right.apply(location));
    }

    @Override
    public T apply(Location location, Chunk chunk) {
        return apply(left.apply(location, chunk), right.apply(location, chunk));
    }
}
