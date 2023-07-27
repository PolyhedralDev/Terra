/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.parser.lang.variables;

import com.dfsek.terra.addons.terrascript.lexer.SourcePosition;
import com.dfsek.terra.addons.terrascript.parser.lang.Expression;


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
    public Expression.ReturnType getType() {
        return Expression.ReturnType.NUMBER;
    }
    
    @Override
    public SourcePosition getPosition() {
        return position;
    }
}
