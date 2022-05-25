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


public class ConcatenationOperation extends BinaryOperation<Object, Object> {
    public ConcatenationOperation(Returnable<Object> left, Returnable<Object> right, Position position) {
        super(left, right, position);
    }
    
    @Override
    public String apply(Supplier<Object> left, Supplier<Object> right) {
        return left.get().toString() + right.get().toString();
    }
    
    @Override
    public Returnable.ReturnType returnType() {
        return Returnable.ReturnType.STRING;
    }
}
