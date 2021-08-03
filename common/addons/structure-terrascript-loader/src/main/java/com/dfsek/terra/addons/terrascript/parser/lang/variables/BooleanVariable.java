package com.dfsek.terra.addons.terrascript.parser.lang.variables;

import com.dfsek.terra.addons.terrascript.api.lang.Returnable;
import com.dfsek.terra.addons.terrascript.api.Position;
import com.dfsek.terra.addons.terrascript.api.lang.Variable;

public class BooleanVariable implements Variable<Boolean> {
    private final Position position;
    private Boolean value;

    public BooleanVariable(Boolean value, Position position) {
        this.value = value;
        this.position = position;
    }

    @Override
    public Boolean getValue() {
        return value;
    }

    @Override
    public void setValue(Boolean value) {
        this.value = value;
    }

    @Override
    public Returnable.ReturnType getType() {
        return Returnable.ReturnType.BOOLEAN;
    }

    @Override
    public Position getPosition() {
        return position;
    }
}
