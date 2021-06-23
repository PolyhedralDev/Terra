package com.dfsek.terra.api.structures.parser.lang.operations;

import com.dfsek.terra.api.structures.parser.lang.Returnable;
import com.dfsek.terra.api.structures.tokenizer.Position;

public class ConcatenationOperation extends BinaryOperation<Object, Object> {
    public ConcatenationOperation(Returnable<Object> left, Returnable<Object> right, Position position) {
        super(left, right, position);
    }

    @Override
    public String apply(Object left, Object right) {
        return left.toString() + right.toString();
    }

    @Override
    public ReturnType returnType() {
        return ReturnType.STRING;
    }
}
