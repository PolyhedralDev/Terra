package com.dfsek.terra.addons.terrascript.parser.lang.constants;

import com.dfsek.terra.addons.terrascript.api.lang.Returnable;
import com.dfsek.terra.addons.terrascript.api.Position;

public class NumericConstant extends ConstantExpression<Number> {
    public NumericConstant(Number constant, Position position) {
        super(constant, position);
    }

    @Override
    public Returnable.ReturnType returnType() {
        return Returnable.ReturnType.NUMBER;
    }
}
