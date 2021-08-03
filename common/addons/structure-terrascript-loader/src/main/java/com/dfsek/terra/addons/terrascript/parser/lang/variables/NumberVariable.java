package com.dfsek.terra.addons.terrascript.parser.lang.variables;

import com.dfsek.terra.addons.terrascript.api.lang.Returnable;
import com.dfsek.terra.addons.terrascript.api.Position;
import com.dfsek.terra.addons.terrascript.api.lang.Variable;

public class NumberVariable implements Variable<Number> {
    private final Position position;
    private Number value;

    public NumberVariable(Number value, Position position) {
        this.value = value;
        this.position = position;
    }

    @Override
    public Number getValue() {
        return value;
    }

    @Override
    public void setValue(Number value) {
        this.value = value;
    }

    @Override
    public Returnable.ReturnType getType() {
        return Returnable.ReturnType.NUMBER;
    }

    @Override
    public Position getPosition() {
        return position;
    }
}
