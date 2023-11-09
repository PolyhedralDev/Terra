/*
 * Copyright (c) 2020-2023 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.parser.lang.operations.statements;

import com.dfsek.terra.addons.terrascript.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.parser.lang.Returnable;
import com.dfsek.terra.addons.terrascript.parser.lang.Scope;
import com.dfsek.terra.addons.terrascript.parser.lang.operations.BinaryOperation;
import com.dfsek.terra.addons.terrascript.tokenizer.Position;

import static com.dfsek.terra.api.util.MathUtil.EPSILON;


public class EqualsStatement extends BinaryOperation<Object, Boolean> {

    public EqualsStatement(Returnable<Object> left, Returnable<Object> right, Position position) {
        super(left, right, position);
    }


    @Override
    public Returnable.ReturnType returnType() {
        return Returnable.ReturnType.BOOLEAN;
    }

    @Override
    public Boolean apply(ImplementationArguments implementationArguments, Scope scope) {
        return applyBoolean(implementationArguments, scope);
    }

    @Override
    public boolean applyBoolean(ImplementationArguments implementationArguments, Scope scope) {
        Object leftValue = left.apply(implementationArguments, scope);
        Object rightValue = right.apply(implementationArguments, scope);
        if(leftValue instanceof Number l && rightValue instanceof Number r) {
            return Math.abs(l.doubleValue() - r.doubleValue()) <= EPSILON;
        }

        return leftValue.equals(rightValue);
    }
}
