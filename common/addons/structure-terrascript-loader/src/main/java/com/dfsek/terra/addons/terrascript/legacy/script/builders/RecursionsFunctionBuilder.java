/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.legacy.script.builders;

import java.util.List;

import com.dfsek.terra.addons.terrascript.Type;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.Expression;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.functions.FunctionBuilder;
import com.dfsek.terra.addons.terrascript.legacy.script.functions.RecursionsFunction;
import com.dfsek.terra.addons.terrascript.lexer.SourcePosition;


public class RecursionsFunctionBuilder implements FunctionBuilder<RecursionsFunction> {
    @Override
    public RecursionsFunction build(List<Expression<?>> argumentList, SourcePosition position) {
        return new RecursionsFunction(position);
    }
    
    @Override
    public int argNumber() {
        return 0;
    }
    
    @Override
    public Type getArgument(int position) {
        return null;
    }
}
