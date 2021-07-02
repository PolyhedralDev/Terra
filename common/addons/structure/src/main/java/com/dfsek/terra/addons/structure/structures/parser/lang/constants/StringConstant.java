package com.dfsek.terra.addons.structure.structures.parser.lang.constants;

import com.dfsek.terra.addons.structure.structures.tokenizer.Position;

public class StringConstant extends ConstantExpression<String> {
    public StringConstant(String constant, Position position) {
        super(constant, position);
    }

    @Override
    public ReturnType returnType() {
        return ReturnType.STRING;
    }
}
