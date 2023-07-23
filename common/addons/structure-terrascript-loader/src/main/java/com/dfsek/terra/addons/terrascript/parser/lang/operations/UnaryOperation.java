/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.parser.lang.operations;

import com.dfsek.terra.addons.terrascript.parser.lang.Expression;
import com.dfsek.terra.addons.terrascript.tokenizer.SourcePosition;


public abstract class UnaryOperation<T> implements Expression<T> {
    protected final Expression<T> input;
    private final SourcePosition position;
    
    public UnaryOperation(Expression<T> input, SourcePosition position) {
        this.input = input;
        this.position = position;
    }
    
    @Override
    public SourcePosition getPosition() {
        return position;
    }
}
