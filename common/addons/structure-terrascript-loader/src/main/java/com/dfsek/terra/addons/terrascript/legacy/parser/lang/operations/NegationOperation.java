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


public class NegationOperation extends UnaryOperation<Number> {
    public NegationOperation(Expression<Number> input, SourcePosition position) {
        super(input, position);
    }
    
    @Override
    public Type returnType() {
        return Type.NUMBER;
    }
    
    @Override
    public Number evaluate(ImplementationArguments implementationArguments, Scope scope) {
        return applyDouble(implementationArguments, scope);
    }
    
    @Override
    public double applyDouble(ImplementationArguments implementationArguments, Scope scope) {
        return -input.applyDouble(implementationArguments, scope);
    }
}
