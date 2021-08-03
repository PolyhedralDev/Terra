package com.dfsek.terra.addons.terrascript.parser.lang.operations;

import com.dfsek.terra.addons.terrascript.api.lang.Returnable;
import com.dfsek.terra.addons.terrascript.api.Position;

public class ModuloOperation extends BinaryOperation<Number, Number> {
    public ModuloOperation(Returnable<Number> left, Returnable<Number> right, Position start) {
        super(left, right, start);
    }

    @Override
    public Number apply(Number left, Number right) {
        return left.doubleValue() % right.doubleValue();
    }

    @Override
    public ReturnType returnType() {
        return ReturnType.NUMBER;
    }
}
