package com.dfsek.terra.addons.structure.structures.parser.lang.constants;

import com.dfsek.terra.addons.structure.structures.tokenizer.Position;

public class BooleanConstant extends ConstantExpression<Boolean> {
    public BooleanConstant(Boolean constant, Position position) {
        super(constant, position);
    }

    @Override
    public ReturnType returnType() {
        return ReturnType.BOOLEAN;
    }
}
