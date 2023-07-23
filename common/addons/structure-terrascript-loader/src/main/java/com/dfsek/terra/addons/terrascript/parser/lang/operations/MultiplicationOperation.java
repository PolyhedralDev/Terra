/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.parser.lang.operations;

import com.dfsek.terra.addons.terrascript.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.parser.lang.Expression;
import com.dfsek.terra.addons.terrascript.parser.lang.Scope;
import com.dfsek.terra.addons.terrascript.tokenizer.SourcePosition;


public class MultiplicationOperation extends BinaryOperation<Number, Number> {
    public MultiplicationOperation(Expression<Number> left, Expression<Number> right, SourcePosition position) {
        super(left, right, position);
    }
    
    @Override
    public Number invoke(ImplementationArguments implementationArguments, Scope scope) {
        return applyDouble(implementationArguments, scope);
    }
    
    @Override
    public double applyDouble(ImplementationArguments implementationArguments, Scope scope) {
        return left.applyDouble(implementationArguments, scope) * right.applyDouble(implementationArguments, scope);
    }

    @Override
    public ReturnType returnType() {
        return ReturnType.NUMBER;
    }
}
