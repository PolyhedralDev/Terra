/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.parser.lang.variables;

import com.dfsek.terra.addons.terrascript.parser.lang.Expression;
import com.dfsek.terra.addons.terrascript.tokenizer.SourcePosition;


public interface Variable<T> {
    T getValue();
    
    void setValue(T value);
    
    Expression.ReturnType getType();
    
    SourcePosition getPosition();
}
