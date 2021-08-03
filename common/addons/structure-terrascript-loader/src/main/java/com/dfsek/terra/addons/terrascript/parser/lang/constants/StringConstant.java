package com.dfsek.terra.addons.terrascript.parser.lang.constants;

import com.dfsek.terra.addons.terrascript.api.Position;
import com.dfsek.terra.addons.terrascript.api.lang.Returnable;

public class StringConstant extends ConstantExpression<String> {
    public StringConstant(String constant, Position position) {
        super(constant, position);
    }

    @Override
    public Returnable.ReturnType returnType() {
        return Returnable.ReturnType.STRING;
    }
}
