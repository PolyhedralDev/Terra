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


public class DivisionOperation extends BinaryOperation<Number, Number> {
    public DivisionOperation(Expression<Number> left, Expression<Number> right, SourcePosition position) {
        super(left, right, position);
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
        return left.applyDouble(implementationArguments, scope) / right.applyDouble(implementationArguments, scope);
    }
}
