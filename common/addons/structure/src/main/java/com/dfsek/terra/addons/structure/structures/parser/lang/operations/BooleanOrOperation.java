package com.dfsek.terra.addons.structure.structures.parser.lang.operations;

import com.dfsek.terra.addons.structure.structures.parser.lang.Returnable;
import com.dfsek.terra.addons.structure.structures.tokenizer.Position;

public class BooleanOrOperation extends BinaryOperation<Boolean, Boolean> {
    public BooleanOrOperation(Returnable<Boolean> left, Returnable<Boolean> right, Position start) {
        super(left, right, start);
    }

    @Override
    public Boolean apply(Boolean left, Boolean right) {
        return left || right;
    }

    @Override
    public ReturnType returnType() {
        return ReturnType.BOOLEAN;
    }
}
