package com.dfsek.terra.api.structures.parser.lang.operations.statements;

import com.dfsek.terra.api.structures.parser.lang.Returnable;
import com.dfsek.terra.api.structures.parser.lang.operations.BinaryOperation;
import com.dfsek.terra.api.structures.tokenizer.Position;

public class LessThanStatement extends BinaryOperation<Number, Boolean> {
    public LessThanStatement(Returnable<Number> left, Returnable<Number> right, Position position) {
        super(left, right, position);
    }

    @Override
    public Boolean apply(Number left, Number right) {
        return left.doubleValue() < right.doubleValue();
    }


    @Override
    public ReturnType returnType() {
        return ReturnType.BOOLEAN;
    }
}
