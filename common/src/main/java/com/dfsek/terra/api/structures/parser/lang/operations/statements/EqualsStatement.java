package com.dfsek.terra.api.structures.parser.lang.operations.statements;

import com.dfsek.terra.api.structures.parser.lang.Returnable;
import com.dfsek.terra.api.structures.parser.lang.operations.BinaryOperation;
import com.dfsek.terra.api.structures.tokenizer.Position;

public class EqualsStatement extends BinaryOperation<Object, Boolean> {
    public EqualsStatement(Returnable<Object> left, Returnable<Object> right, Position position) {
        super(left, right, position);
    }

    @Override
    public Boolean apply(Object left, Object right) {
        return left.equals(right);
    }


    @Override
    public ReturnType returnType() {
        return ReturnType.BOOLEAN;
    }
}
