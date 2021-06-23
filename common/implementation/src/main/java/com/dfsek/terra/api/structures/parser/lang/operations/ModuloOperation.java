package com.dfsek.terra.api.structures.parser.lang.operations;

import com.dfsek.terra.api.structures.parser.lang.Returnable;
import com.dfsek.terra.api.structures.tokenizer.Position;

public class ModuloOperation extends BinaryOperation<Number, Number> {
    public ModuloOperation(Returnable<Number> left, Returnable<Number> right, Position start) {
        super(left, right, start);
    }

    @Override
    public Number apply(Number left, Number right) {
        return left.doubleValue() % right.doubleValue();
    }

    @Override
    public ReturnType returnType() {
        return ReturnType.NUMBER;
    }
}
