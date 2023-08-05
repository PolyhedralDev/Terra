/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.legacy.parser.lang.functions;

import com.dfsek.terra.addons.terrascript.legacy.parser.lang.Expression;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.Scope;


public interface Function<T> extends Expression<T> {
    
    @Override
    default double applyDouble(ImplementationArguments implementationArguments, Scope scope) {
        return ((Number) evaluate(implementationArguments, scope)).doubleValue();
    }
    
    @Override
    default boolean applyBoolean(ImplementationArguments implementationArguments, Scope scope) {
        return (Boolean) evaluate(implementationArguments, scope);
    }
}
