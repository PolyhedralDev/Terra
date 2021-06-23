package com.dfsek.terra.api.structures.parser.lang.constants;

import com.dfsek.terra.api.structures.tokenizer.Position;

public class BooleanConstant extends ConstantExpression<Boolean> {
    public BooleanConstant(Boolean constant, Position position) {
        super(constant, position);
    }

    @Override
    public ReturnType returnType() {
        return ReturnType.BOOLEAN;
    }
}
