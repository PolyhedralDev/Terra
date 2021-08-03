package com.dfsek.terra.addons.terrascript.parser.lang.variables;

import com.dfsek.terra.addons.terrascript.api.Returnable;
import com.dfsek.terra.addons.terrascript.api.Position;
import com.dfsek.terra.addons.terrascript.api.Variable;

public class StringVariable implements Variable<String> {
    private final Position position;
    private String value;

    public StringVariable(String value, Position position) {
        this.value = value;
        this.position = position;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public Returnable.ReturnType getType() {
        return Returnable.ReturnType.STRING;
    }

    @Override
    public Position getPosition() {
        return position;
    }
}
