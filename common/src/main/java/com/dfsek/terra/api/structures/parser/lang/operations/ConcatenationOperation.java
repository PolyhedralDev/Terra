package com.dfsek.terra.api.structures.parser.lang.operations;

public class ConcatenationOperation implements BinaryOperation<Object> {
    @Override
    public String apply(Object left, Object right) {
        return left.toString() + right.toString();
    }
}
