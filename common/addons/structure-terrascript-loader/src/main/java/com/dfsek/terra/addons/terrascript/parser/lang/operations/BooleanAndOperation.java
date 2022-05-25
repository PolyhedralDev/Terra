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


public class BooleanAndOperation extends BinaryOperation<Boolean, Boolean> {
    public BooleanAndOperation(Returnable<Boolean> left, Returnable<Boolean> right, Position start) {
        super(left, right, start);
    }
    
    @Override
    public Boolean apply(Supplier<Boolean> left, Supplier<Boolean> right) {
        return left.get() && right.get();
    }
    
    @Override
    public ReturnType returnType() {
        return ReturnType.BOOLEAN;
    }
}
