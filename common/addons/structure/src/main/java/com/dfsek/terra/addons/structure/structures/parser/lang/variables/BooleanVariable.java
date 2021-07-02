package com.dfsek.terra.addons.structure.structures.parser.lang.variables;

import com.dfsek.terra.addons.structure.structures.parser.lang.Returnable;
import com.dfsek.terra.addons.structure.structures.tokenizer.Position;

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
