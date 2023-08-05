/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.legacy.parser.lang.variables;

import com.dfsek.terra.addons.terrascript.Type;
import com.dfsek.terra.addons.terrascript.lexer.SourcePosition;


public class NumberVariable implements Variable<Number> {
    private final SourcePosition position;
    private Number value;
    
    public NumberVariable(Number value, SourcePosition position) {
        this.value = value;
        this.position = position;
    }
    
    @Override
    public Number getValue() {
        return value;
    }
    
    @Override
    public void setValue(Number value) {
        this.value = value;
    }
    
    @Override
    public Type getType() {
        return Type.NUMBER;
    }
    
    @Override
    public SourcePosition getPosition() {
        return position;
    }
}
