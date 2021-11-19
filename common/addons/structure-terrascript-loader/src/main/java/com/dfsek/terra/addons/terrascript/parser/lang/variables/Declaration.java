/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.parser.lang.variables;

import java.util.Map;

import com.dfsek.terra.addons.terrascript.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.parser.lang.Item;
import com.dfsek.terra.addons.terrascript.parser.lang.Returnable;
import com.dfsek.terra.addons.terrascript.tokenizer.Position;


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
    public T apply(ImplementationArguments implementationArguments, Map<String, Variable<?>> variableMap) {
        T result = value.apply(implementationArguments, variableMap);
        switch(type) {
            case NUMBER -> variableMap.put(identifier, new NumberVariable((Number) result, position));
            case BOOLEAN -> variableMap.put(identifier, new BooleanVariable((Boolean) result, position));
            case STRING -> variableMap.put(identifier, new StringVariable((String) result, position));
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
