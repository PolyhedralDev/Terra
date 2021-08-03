package com.dfsek.terra.addons.terrascript.parser.lang.operations;

import com.dfsek.terra.addons.terrascript.api.Position;
import com.dfsek.terra.addons.terrascript.api.lang.Returnable;

public class MultiplicationOperation extends BinaryOperation<Number, Number> {
    public MultiplicationOperation(Returnable<Number> left, Returnable<Number> right, Position position) {
        super(left, right, position);
    }

    @Override
    public Number apply(Number left, Number right) {
        return left.doubleValue() * right.doubleValue();
    }

    @Override
    public ReturnType returnType() {
        return ReturnType.NUMBER;
    }
}
