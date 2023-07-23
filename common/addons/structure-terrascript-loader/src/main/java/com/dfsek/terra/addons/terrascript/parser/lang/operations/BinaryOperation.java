/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.parser.lang.operations;

import com.dfsek.terra.addons.terrascript.parser.lang.Expression;
import com.dfsek.terra.addons.terrascript.tokenizer.SourcePosition;


public abstract class BinaryOperation<I, O> implements Expression<O> {
    protected final Expression<I> left;
    protected final Expression<I> right;
    private final SourcePosition start;
    
    public BinaryOperation(Expression<I> left, Expression<I> right, SourcePosition start) {
        this.left = left;
        this.right = right;
        this.start = start;
    }
    
    @Override
    public SourcePosition getPosition() {
        return start;
    }
}
