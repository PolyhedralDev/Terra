/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.parser.lang.operations;

import com.dfsek.terra.addons.terrascript.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.parser.lang.Returnable;
import com.dfsek.terra.addons.terrascript.parser.lang.Scope;
import com.dfsek.terra.addons.terrascript.tokenizer.Position;

import java.util.function.Supplier;


public abstract class BinaryOperation<I, O> implements Returnable<O> {
    private final Returnable<I> left;
    private final Returnable<I> right;
    private final Position start;
    
    public BinaryOperation(Returnable<I> left, Returnable<I> right, Position start) {
        this.left = left;
        this.right = right;
        this.start = start;
    }
    
    public abstract O apply(Supplier<I> left, Supplier<I> right);
    
    @Override
    public O apply(ImplementationArguments implementationArguments, Scope scope) {
        return apply(() -> left.apply(implementationArguments, scope), () -> right.apply(implementationArguments, scope));
    }
    
    @Override
    public Position getPosition() {
        return start;
    }
}
