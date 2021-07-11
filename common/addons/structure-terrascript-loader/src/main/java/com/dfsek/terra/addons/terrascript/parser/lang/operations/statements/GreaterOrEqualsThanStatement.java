package com.dfsek.terra.addons.terrascript.parser.lang.operations.statements;

import com.dfsek.terra.addons.terrascript.parser.lang.Returnable;
import com.dfsek.terra.addons.terrascript.parser.lang.operations.BinaryOperation;
import com.dfsek.terra.addons.terrascript.tokenizer.Position;

public class GreaterOrEqualsThanStatement extends BinaryOperation<Number, Boolean> {
    public GreaterOrEqualsThanStatement(Returnable<Number> left, Returnable<Number> right, Position position) {
        super(left, right, position);
    }

    @Override
    public Boolean apply(Number left, Number right) {
        return left.doubleValue() >= right.doubleValue();
    }


    @Override
    public Returnable.ReturnType returnType() {
        return Returnable.ReturnType.BOOLEAN;
    }
}
