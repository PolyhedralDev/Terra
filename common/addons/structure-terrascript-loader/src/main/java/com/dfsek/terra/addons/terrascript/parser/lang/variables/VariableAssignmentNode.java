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


public class VariableAssignmentNode<T> implements Item<T> {
    private final Returnable<T> value;
    private final Position position;
    private final String identifier;
    
    public VariableAssignmentNode(Returnable<T> value, String identifier, Position position) {
        this.value = value;
        this.identifier = identifier;
        this.position = position;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public synchronized T apply(ImplementationArguments implementationArguments, Scope scope) {
        T val = value.apply(implementationArguments, scope);
        ((Variable<T>) scope.get(identifier)).setValue(val);
        return val;
    }
    
    @Override
    public Position getPosition() {
        return position;
    }
}
