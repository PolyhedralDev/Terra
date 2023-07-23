/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.parser.lang.variables;

import com.dfsek.terra.addons.terrascript.parser.lang.Expression;
import com.dfsek.terra.addons.terrascript.tokenizer.SourcePosition;


public class BooleanVariable implements Variable<Boolean> {
    private final SourcePosition position;
    private Boolean value;
    
    public BooleanVariable(Boolean value, SourcePosition position) {
        this.value = value;
        this.position = position;
    }
    
    @Override
    public Boolean getValue() {
        return value;
    }
    
    @Override
    public void setValue(Boolean value) {
        this.value = value;
    }
    
    @Override
    public Expression.ReturnType getType() {
        return Expression.ReturnType.BOOLEAN;
    }
    
    @Override
    public SourcePosition getPosition() {
        return position;
    }
}
