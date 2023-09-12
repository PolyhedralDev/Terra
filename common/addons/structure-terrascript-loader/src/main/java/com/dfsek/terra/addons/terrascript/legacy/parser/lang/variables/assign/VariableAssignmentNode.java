/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.legacy.parser.lang.variables.assign;

import com.dfsek.terra.addons.terrascript.legacy.parser.lang.Expression;
import com.dfsek.terra.addons.terrascript.lexer.SourcePosition;


public abstract class VariableAssignmentNode<T> implements Expression<T> {
    protected final Expression<T> value;
    protected final int index;
    private final SourcePosition position;
    
    
    public VariableAssignmentNode(Expression<T> value, SourcePosition position, int index) {
        this.value = value;
        this.index = index;
        this.position = position;
    }
    
    @Override
    public SourcePosition getPosition() {
        return position;
    }
}