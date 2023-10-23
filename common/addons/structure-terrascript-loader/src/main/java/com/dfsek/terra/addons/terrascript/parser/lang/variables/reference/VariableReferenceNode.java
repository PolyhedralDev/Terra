/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.parser.lang.variables.reference;

import com.dfsek.terra.addons.terrascript.parser.lang.Returnable;
import com.dfsek.terra.addons.terrascript.tokenizer.Position;


public abstract class VariableReferenceNode<T> implements Returnable<T> {
    protected final int index;
    private final Position position;
    private final ReturnType type;
    
    public VariableReferenceNode(Position position, ReturnType type, int index) {
        this.position = position;
        this.type = type;
        this.index = index;
    }
    
    @Override
    public ReturnType returnType() {
        return type;
    }
    
    @Override
    public Position getPosition() {
        return position;
    }
}
