/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.parser.lang.operations.statements;

import net.jafama.FastMath;

import com.dfsek.terra.addons.terrascript.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.parser.lang.Expression;
import com.dfsek.terra.addons.terrascript.parser.lang.Scope;
import com.dfsek.terra.addons.terrascript.parser.lang.operations.BinaryOperation;
import com.dfsek.terra.addons.terrascript.tokenizer.SourcePosition;

import static com.dfsek.terra.api.util.MathUtil.EPSILON;


public class NotEqualsStatement extends BinaryOperation<Object, Boolean> {
    public NotEqualsStatement(Expression<Object> left, Expression<Object> right, SourcePosition position) {
        super(left, right, position);
    }
    
    @Override
    public Boolean evaluate(ImplementationArguments implementationArguments, Scope scope) {
        return applyBoolean(implementationArguments, scope);
    }
    
    @Override
    public boolean applyBoolean(ImplementationArguments implementationArguments, Scope scope) {
        Object leftValue = left.evaluate(implementationArguments, scope);
        Object rightValue = right.evaluate(implementationArguments, scope);
        if(leftValue instanceof Number l && rightValue instanceof Number r) {
            return FastMath.abs(l.doubleValue() - r.doubleValue()) > EPSILON;
        }
        
        return !leftValue.equals(rightValue);
    }
    
    @Override
    public Expression.ReturnType returnType() {
        return Expression.ReturnType.BOOLEAN;
    }
}
