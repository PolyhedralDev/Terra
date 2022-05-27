/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.parser.lang.operations;

import java.util.function.Supplier;

import com.dfsek.terra.addons.terrascript.parser.lang.Returnable;
import com.dfsek.terra.addons.terrascript.tokenizer.Position;


public class NumberAdditionOperation extends BinaryOperation<Number, Number> {
    public NumberAdditionOperation(Returnable<Number> left, Returnable<Number> right, Position position) {
        super(left, right, position);
    }
    
    @Override
    public Number apply(Supplier<Number> left, Supplier<Number> right) {
        return left.get().doubleValue() + right.get().doubleValue();
    }
    
    @Override
    public ReturnType returnType() {
        return ReturnType.NUMBER;
    }
}
