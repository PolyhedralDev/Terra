/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.parser.lang.operations;

import com.dfsek.terra.addons.terrascript.parser.lang.Returnable;
import com.dfsek.terra.addons.terrascript.tokenizer.Position;

import java.util.function.Supplier;


public class DivisionOperation extends BinaryOperation<Number, Number> {
    public DivisionOperation(Returnable<Number> left, Returnable<Number> right, Position position) {
        super(left, right, position);
    }
    
    @Override
    public Number apply(Supplier<Number> left, Supplier<Number> right) {
        return left.get().doubleValue() / right.get().doubleValue();
    }
    
    @Override
    public Returnable.ReturnType returnType() {
        return Returnable.ReturnType.NUMBER;
    }
}
