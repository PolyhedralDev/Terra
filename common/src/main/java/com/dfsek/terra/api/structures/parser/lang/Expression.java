package com.dfsek.terra.api.structures.parser.lang;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.structures.parser.lang.operations.BinaryOperation;

public class Expression<T> implements Executable<T> {
    private final ReturnType type;
    private final Executable<T> left;
    private final Executable<T> right;
    private final BinaryOperation<T> operation;

    public Expression(ReturnType type, Executable<T> left, Executable<T> right, BinaryOperation<T> operation) {
        this.type = type;
        this.left = left;
        this.right = right;
        this.operation = operation;
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
}
