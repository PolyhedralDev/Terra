/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.parser.lang.operations.statements;

import com.dfsek.terra.addons.terrascript.parser.lang.Returnable;
import com.dfsek.terra.addons.terrascript.parser.lang.operations.BinaryOperation;
import com.dfsek.terra.addons.terrascript.tokenizer.Position;

import net.jafama.FastMath;

import java.util.function.Supplier;

import static com.dfsek.terra.api.util.MathUtil.EPSILON;


public class NotEqualsStatement extends BinaryOperation<Object, Boolean> {
    public NotEqualsStatement(Returnable<Object> left, Returnable<Object> right, Position position) {
        super(left, right, position);
    }
    
    @Override
    public Boolean apply(Supplier<Object> left, Supplier<Object> right) {
        Object leftUnwrapped = left.get();
        Object rightUnwrapped = right.get();
        if(leftUnwrapped instanceof Number l && rightUnwrapped instanceof Number r) {
            return FastMath.abs(l.doubleValue() - r.doubleValue()) > EPSILON;
        }
    
        return !leftUnwrapped.equals(rightUnwrapped);
    }
    
    
    @Override
    public Returnable.ReturnType returnType() {
        return Returnable.ReturnType.BOOLEAN;
    }
}
