package com.dfsek.terra.addons.terrascript.script.functions;

import com.dfsek.terra.addons.terrascript.api.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.api.Function;
import com.dfsek.terra.addons.terrascript.api.Variable;
import com.dfsek.terra.addons.terrascript.script.TerraImplementationArguments;
import com.dfsek.terra.addons.terrascript.api.Position;

import java.util.Map;

public class RecursionsFunction implements Function<Number> {
    private final Position position;

    public RecursionsFunction(Position position) {
        this.position = position;
    }

    @Override
    public ReturnType returnType() {
        return ReturnType.NUMBER;
    }

    @Override
    public Number apply(ImplementationArguments implementationArguments, Map<String, Variable<?>> variableMap) {
        return ((TerraImplementationArguments) implementationArguments).getRecursions();
    }

    @Override
    public Position getPosition() {
        return position;
    }
}
