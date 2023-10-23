/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.parser.lang;

import com.dfsek.terra.addons.terrascript.tokenizer.Position;


public interface Item<T> {
    T apply(ImplementationArguments implementationArguments, Scope scope);
    
    default double applyDouble(ImplementationArguments implementationArguments, Scope scope) {
        throw new UnsupportedOperationException("Cannot apply " + this + " as double");
    }
    
    default boolean applyBoolean(ImplementationArguments implementationArguments, Scope scope) {
        throw new UnsupportedOperationException("Cannot apply " + this + " as double");
    }
    
    Position getPosition();
}
