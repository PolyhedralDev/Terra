/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.parser.lang.operations;

import com.dfsek.terra.addons.terrascript.parser.lang.Returnable;
import com.dfsek.terra.addons.terrascript.tokenizer.Position;


public abstract class UnaryOperation<T> implements Returnable<T> {
    protected final Returnable<T> input;
    private final Position position;
    
    public UnaryOperation(Returnable<T> input, Position position) {
        this.input = input;
        this.position = position;
    }
    
    @Override
    public Position getPosition() {
        return position;
    }
}
