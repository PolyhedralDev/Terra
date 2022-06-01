/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.parser.lang.variables;

import com.dfsek.terra.addons.terrascript.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.parser.lang.Item;
import com.dfsek.terra.addons.terrascript.parser.lang.Returnable;
import com.dfsek.terra.addons.terrascript.parser.lang.Scope;
import com.dfsek.terra.addons.terrascript.tokenizer.Position;


public class VariableDeclarationNode<T> implements Item<T> {
    private final Position position;
    private final String identifier;
    private final Returnable<T> value;
    private final Returnable.ReturnType type;
    
    public VariableDeclarationNode(Position position, String identifier, Returnable<T> value, Returnable.ReturnType type) {
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
    public T apply(ImplementationArguments implementationArguments, Scope scope) {
        T result = value.apply(implementationArguments, scope);
        scope.put(identifier, switch(type) {
            case NUMBER -> new NumberVariable((Number) result, position);
            case BOOLEAN -> new BooleanVariable((Boolean) result, position);
            case STRING -> new StringVariable((String) result, position);
            default -> throw new IllegalStateException("Unexpected value: " + type);
        });
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
