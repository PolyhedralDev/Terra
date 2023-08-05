/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.legacy.parser.lang.variables;

import com.dfsek.terra.addons.terrascript.Type;
import com.dfsek.terra.addons.terrascript.lexer.SourcePosition;


public class StringVariable implements Variable<String> {
    private final SourcePosition position;
    private String value;
    
    public StringVariable(String value, SourcePosition position) {
        this.value = value;
        this.position = position;
    }
    
    @Override
    public String getValue() {
        return value;
    }
    
    @Override
    public void setValue(String value) {
        this.value = value;
    }
    
    @Override
    public Type getType() {
        return Type.STRING;
    }
    
    @Override
    public SourcePosition getPosition() {
        return position;
    }
}
