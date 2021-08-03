package com.dfsek.terra.addons.terrascript.parser.lang.operations.statements;

import com.dfsek.terra.addons.terrascript.api.Position;
import com.dfsek.terra.addons.terrascript.api.lang.Returnable;
import com.dfsek.terra.addons.terrascript.parser.lang.operations.BinaryOperation;

public class NotEqualsStatement extends BinaryOperation<Object, Boolean> {
    public NotEqualsStatement(Returnable<Object> left, Returnable<Object> right, Position position) {
        super(left, right, position);
    }

    @Override
    public Boolean apply(Object left, Object right) {
        return !left.equals(right);
    }


    @Override
    public Returnable.ReturnType returnType() {
        return Returnable.ReturnType.BOOLEAN;
    }
}
