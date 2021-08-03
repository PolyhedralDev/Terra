package com.dfsek.terra.addons.terrascript.parser.lang.operations;

import com.dfsek.terra.addons.terrascript.api.Position;
import com.dfsek.terra.addons.terrascript.api.lang.Returnable;

public class BooleanAndOperation extends BinaryOperation<Boolean, Boolean> {
    public BooleanAndOperation(Returnable<Boolean> left, Returnable<Boolean> right, Position start) {
        super(left, right, start);
    }

    @Override
    public Boolean apply(Boolean left, Boolean right) {
        return left && right;
    }

    @Override
    public ReturnType returnType() {
        return ReturnType.BOOLEAN;
    }
}
