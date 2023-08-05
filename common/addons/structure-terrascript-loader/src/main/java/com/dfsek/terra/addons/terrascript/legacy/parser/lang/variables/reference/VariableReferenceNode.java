/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.legacy.parser.lang.variables.reference;

import com.dfsek.terra.addons.terrascript.Type;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.Expression;
import com.dfsek.terra.addons.terrascript.lexer.SourcePosition;


public abstract class VariableReferenceNode<T> implements Expression<T> {
    protected final int index;
    private final SourcePosition position;
    private final Type type;
    
    public VariableReferenceNode(SourcePosition position, Type type, int index) {
        this.position = position;
        this.type = type;
        this.index = index;
    }
    
    @Override
    public Type returnType() {
        return type;
    }
    
    @Override
    public SourcePosition getPosition() {
        return position;
    }
}
