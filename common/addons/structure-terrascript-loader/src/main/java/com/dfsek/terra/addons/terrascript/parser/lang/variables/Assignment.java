package com.dfsek.terra.addons.terrascript.parser.lang.variables;

import com.dfsek.terra.addons.terrascript.api.Position;
import com.dfsek.terra.addons.terrascript.api.lang.Item;
import com.dfsek.terra.addons.terrascript.api.lang.Returnable;
import com.dfsek.terra.addons.terrascript.api.lang.Variable;
import com.dfsek.terra.api.properties.Context;

import java.util.Map;

public class Assignment<T> implements Item<T> {
    private final Returnable<T> value;
    private final Position position;
    private final String identifier;

    public Assignment(Returnable<T> value, String identifier, Position position) {
        this.value = value;
        this.identifier = identifier;
        this.position = position;
    }

    @SuppressWarnings("unchecked")
    @Override
    public synchronized T apply(Context context, Map<String, Variable<?>> variableMap) {
        T val = value.apply(, implementationArguments, variableMap);
        ((Variable<T>) variableMap.get(identifier)).setValue(val);
        return val;
    }

    @Override
    public Position getPosition() {
        return position;
    }
}
