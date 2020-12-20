package com.dfsek.terra.api.structures.parser.lang.operations;

public class NumberAdditionOperation implements BinaryOperation<Number> {
    @Override
    public Number apply(Number left, Number right) {
        return left.doubleValue() + right.doubleValue();
    }
}
