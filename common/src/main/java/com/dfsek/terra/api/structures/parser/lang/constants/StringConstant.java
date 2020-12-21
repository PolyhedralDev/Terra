package com.dfsek.terra.api.structures.parser.lang.constants;

import com.dfsek.terra.api.structures.tokenizer.Position;

public class StringConstant extends ConstantExpression<String> {
    public StringConstant(String constant, Position position) {
        super(constant, position);
    }

    @Override
    public ReturnType returnType() {
        return ReturnType.STRING;
    }
}
