/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.parser.lang.variables;

import com.dfsek.terra.addons.terrascript.parser.lang.Returnable;
import com.dfsek.terra.addons.terrascript.tokenizer.Position;


public class NumberVariable implements Variable<Number> {
    private final Position position;
    private Number value;
    
    public NumberVariable(Number value, Position position) {
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
    public Returnable.ReturnType getType() {
        return Returnable.ReturnType.NUMBER;
    }
    
    @Override
    public Position getPosition() {
        return position;
    }
}
