package com.dfsek.terra.addons.terrascript.parser.lang.operations.statements;

import com.dfsek.terra.addons.terrascript.api.Position;
import com.dfsek.terra.addons.terrascript.api.lang.Returnable;
import com.dfsek.terra.addons.terrascript.parser.lang.operations.BinaryOperation;

public class LessThanOrEqualsStatement extends BinaryOperation<Number, Boolean> {
    public LessThanOrEqualsStatement(Returnable<Number> left, Returnable<Number> right, Position position) {
        super(left, right, position);
    }

    @Override
    public Boolean apply(Number left, Number right) {
        return left.doubleValue() <= right.doubleValue();
    }


    @Override
    public Returnable.ReturnType returnType() {
        return Returnable.ReturnType.BOOLEAN;
    }
}
