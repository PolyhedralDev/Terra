/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.legacy.parser.lang.operations;

import com.dfsek.terra.addons.terrascript.Type;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.Expression;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.Scope;
import com.dfsek.terra.addons.terrascript.lexer.SourcePosition;


public class BooleanNotOperation extends UnaryOperation<Boolean> {
    public BooleanNotOperation(Expression<Boolean> input, SourcePosition position) {
        super(input, position);
    }
    
    @Override
    public Boolean evaluate(ImplementationArguments implementationArguments, Scope scope) {
        return applyBoolean(implementationArguments, scope);
    }
    
    @Override
    public boolean applyBoolean(ImplementationArguments implementationArguments, Scope scope) {
        return !input.applyBoolean(implementationArguments, scope);
    }
    
    @Override
    public Type returnType() {
        return Type.BOOLEAN;
    }
}
