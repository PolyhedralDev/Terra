package com.dfsek.terra.addons.terrascript.parser.lang.operations;

import com.dfsek.terra.addons.terrascript.parser.lang.Returnable;
import com.dfsek.terra.addons.terrascript.tokenizer.Position;

public class DivisionOperation extends BinaryOperation<Number, Number> {
    public DivisionOperation(Returnable<Number> left, Returnable<Number> right, Position position) {
        super(left, right, position);
    }

    @Override
    public Number apply(Number left, Number right) {
        return left.doubleValue() / right.doubleValue();
    }

    @Override
    public Returnable.ReturnType returnType() {
        return Returnable.ReturnType.NUMBER;
    }
}
