/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.legacy.parser.lang.variables;

import com.dfsek.terra.addons.terrascript.Type;
import com.dfsek.terra.addons.terrascript.lexer.SourcePosition;


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
    public Type getType() {
        return Type.BOOLEAN;
    }
    
    @Override
    public SourcePosition getPosition() {
        return position;
    }
}
