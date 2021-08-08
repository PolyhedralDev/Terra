package com.dfsek.terra.addons.terrascript.parser.lang.variables;

import com.dfsek.terra.addons.terrascript.api.Position;
import com.dfsek.terra.addons.terrascript.api.lang.Item;
import com.dfsek.terra.addons.terrascript.api.lang.Returnable;
import com.dfsek.terra.addons.terrascript.api.lang.Variable;
import com.dfsek.terra.api.properties.Context;

import java.util.Map;

public class Declaration<T> implements Item<T> {
    private final Position position;
    private final String identifier;
    private final Returnable<T> value;
    private final Returnable.ReturnType type;

    public Declaration(Position position, String identifier, Returnable<T> value, Returnable.ReturnType type) {
        switch(type) {
            case STRING:
            case BOOLEAN:
            case NUMBER:
                break;
            default:
                throw new IllegalArgumentException("Invalid variable type: " + type);
        }
        this.position = position;
        this.identifier = identifier;
        this.value = value;
        this.type = type;
    }

    @Override
    public T apply(Context context, Map<String, Variable<?>> variableMap) {
        T result = value.apply(, implementationArguments, variableMap);
        switch(type) {
            case NUMBER:
                variableMap.put(identifier, new NumberVariable((Number) result, position));
                break;
            case BOOLEAN:
                variableMap.put(identifier, new BooleanVariable((Boolean) result, position));
                break;
            case STRING:
                variableMap.put(identifier, new StringVariable((String) result, position));
                break;
        }
        return result;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    public Returnable.ReturnType getType() {
        return type;
    }

    public String getIdentifier() {
        return identifier;
    }
}
