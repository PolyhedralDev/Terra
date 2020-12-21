package com.dfsek.terra.api.structures.parser.lang;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.structures.parser.lang.operations.BinaryOperation;
import com.dfsek.terra.api.structures.tokenizer.Position;

public class Expression<T> implements Returnable<T> {
    private final ReturnType type;
    private final Returnable<T> left;
    private final Returnable<T> right;
    private final BinaryOperation<T> operation;
    private final Position position;

    public Expression(ReturnType type, Returnable<T> left, Returnable<T> right, BinaryOperation<T> operation, Position position) {
        this.type = type;
        this.left = left;
        this.right = right;
        this.operation = operation;
        this.position = position;
    }

    @Override
    public ReturnType returnType() {
        return type;
    }

    @Override
    public T apply(Location location) {
        return operation.apply(left.apply(location), right.apply(location));
    }

    @Override
    public T apply(Location location, Chunk chunk) {
        return operation.apply(left.apply(location, chunk), right.apply(location, chunk));
    }

    @Override
    public Position getPosition() {
        return position;
    }
}
