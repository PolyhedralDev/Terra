package com.dfsek.terra.addons.terrascript.parser.lang.constants;

import com.dfsek.terra.addons.terrascript.tokenizer.Position;


public class BooleanConstant extends ConstantExpression<Boolean> {
    public BooleanConstant(Boolean constant, Position position) {
        super(constant, position);
    }
    
    @Override
    public ReturnType returnType() {
        return ReturnType.BOOLEAN;
    }
}
