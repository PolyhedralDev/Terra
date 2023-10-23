/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.script.builders;

import java.util.List;

import com.dfsek.terra.addons.terrascript.parser.lang.Returnable;
import com.dfsek.terra.addons.terrascript.parser.lang.functions.FunctionBuilder;
import com.dfsek.terra.addons.terrascript.script.functions.RecursionsFunction;
import com.dfsek.terra.addons.terrascript.tokenizer.Position;


public class RecursionsFunctionBuilder implements FunctionBuilder<RecursionsFunction> {
    @Override
    public RecursionsFunction build(List<Returnable<?>> argumentList, Position position) {
        return new RecursionsFunction(position);
    }
    
    @Override
    public int argNumber() {
        return 0;
    }
    
    @Override
    public Returnable.ReturnType getArgument(int position) {
        return null;
    }
}
