/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.parser.lang.variables.assign;

import com.dfsek.terra.addons.terrascript.parser.lang.Item;
import com.dfsek.terra.addons.terrascript.parser.lang.Returnable;
import com.dfsek.terra.addons.terrascript.tokenizer.Position;


public abstract class VariableAssignmentNode<T> implements Item<T> {
    protected final Returnable<T> value;
    protected final int index;
    private final Position position;
    
    
    public VariableAssignmentNode(Returnable<T> value, Position position, int index) {
        this.value = value;
        this.index = index;
        this.position = position;
    }
    
    @Override
    public Position getPosition() {
        return position;
    }
}
