package com.dfsek.terra.addons.structure.structures.parser.lang.operations.statements;

import com.dfsek.terra.addons.structure.structures.parser.lang.Returnable;
import com.dfsek.terra.addons.structure.structures.parser.lang.operations.BinaryOperation;
import com.dfsek.terra.addons.structure.structures.tokenizer.Position;

public class GreaterOrEqualsThanStatement extends BinaryOperation<Number, Boolean> {
    public GreaterOrEqualsThanStatement(Returnable<Number> left, Returnable<Number> right, Position position) {
        super(left, right, position);
    }

    @Override
    public Boolean apply(Number left, Number right) {
        return left.doubleValue() >= right.doubleValue();
    }


    @Override
    public ReturnType returnType() {
        return ReturnType.BOOLEAN;
    }
}
