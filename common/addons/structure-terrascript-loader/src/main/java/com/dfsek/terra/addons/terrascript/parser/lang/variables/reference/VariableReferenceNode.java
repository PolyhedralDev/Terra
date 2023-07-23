/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.parser.lang.variables.reference;

import com.dfsek.terra.addons.terrascript.parser.lang.Expression;
import com.dfsek.terra.addons.terrascript.tokenizer.SourcePosition;


public abstract class VariableReferenceNode<T> implements Expression<T> {
    protected final int index;
    private final SourcePosition position;
    private final ReturnType type;
    
    public VariableReferenceNode(SourcePosition position, ReturnType type, int index) {
        this.position = position;
        this.type = type;
        this.index = index;
    }
    
    @Override
    public ReturnType returnType() {
        return type;
    }
    
    @Override
    public SourcePosition getPosition() {
        return position;
    }
}
