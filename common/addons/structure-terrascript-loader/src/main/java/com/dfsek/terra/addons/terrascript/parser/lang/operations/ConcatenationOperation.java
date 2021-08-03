package com.dfsek.terra.addons.terrascript.parser.lang.operations;

import com.dfsek.terra.addons.terrascript.api.Returnable;
import com.dfsek.terra.addons.terrascript.api.Position;

public class ConcatenationOperation extends BinaryOperation<Object, Object> {
    public ConcatenationOperation(Returnable<Object> left, Returnable<Object> right, Position position) {
        super(left, right, position);
    }

    @Override
    public String apply(Object left, Object right) {
        return left.toString() + right.toString();
    }

    @Override
    public Returnable.ReturnType returnType() {
        return Returnable.ReturnType.STRING;
    }
}
