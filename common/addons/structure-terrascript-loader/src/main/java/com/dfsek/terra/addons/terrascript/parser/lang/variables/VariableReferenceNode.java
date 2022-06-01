/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.parser.lang.variables;

import com.dfsek.terra.addons.terrascript.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.parser.lang.Returnable;
import com.dfsek.terra.addons.terrascript.parser.lang.Scope;
import com.dfsek.terra.addons.terrascript.tokenizer.Position;


public class VariableReferenceNode implements Returnable<Object> {
    private final String identifier;
    private final Position position;
    private final ReturnType type;
    
    public VariableReferenceNode(String identifier, Position position, ReturnType type) {
        this.identifier = identifier;
        this.position = position;
        this.type = type;
    }
    
    @Override
    public ReturnType returnType() {
        return type;
    }
    
    @Override
    public synchronized Object apply(ImplementationArguments implementationArguments, Scope scope) {
        return scope.get(identifier).getValue();
    }
    
    @Override
    public Position getPosition() {
        return position;
    }
}
