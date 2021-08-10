package com.dfsek.terra.addons.terrascript.functions;

import com.dfsek.terra.addons.terrascript.api.Function;
import com.dfsek.terra.addons.terrascript.api.Position;
import com.dfsek.terra.addons.terrascript.api.lang.Variable;
import com.dfsek.terra.addons.terrascript.api.TerraProperties;
import com.dfsek.terra.api.properties.Context;

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
    public Number apply(Context context, Map<String, Variable<?>> variableMap) {
        return context.get(TerraProperties.class).getRecursions();
    }

    @Override
    public Position getPosition() {
        return position;
    }
}
