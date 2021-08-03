package com.dfsek.terra.addons.terrascript.parser.lang.operations;

import com.dfsek.terra.addons.terrascript.api.Position;
import com.dfsek.terra.addons.terrascript.api.lang.Returnable;

public class NegationOperation extends UnaryOperation<Number> {
    public NegationOperation(Returnable<Number> input, Position position) {
        super(input, position);
    }

    @Override
    public Number apply(Number input) {
        return -input.doubleValue();
    }

    @Override
    public ReturnType returnType() {
        return ReturnType.NUMBER;
    }
}
