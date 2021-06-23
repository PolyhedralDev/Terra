package com.dfsek.terra.api.structures.parser.lang.constants;

import com.dfsek.terra.api.structures.tokenizer.Position;

public class NumericConstant extends ConstantExpression<Number> {
    public NumericConstant(Number constant, Position position) {
        super(constant, position);
    }

    @Override
    public ReturnType returnType() {
        return ReturnType.NUMBER;
    }
}
