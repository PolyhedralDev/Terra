/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.script.builders;

import java.util.List;

import com.dfsek.terra.addons.terrascript.lexer.SourcePosition;
import com.dfsek.terra.addons.terrascript.parser.lang.Expression;
import com.dfsek.terra.addons.terrascript.parser.lang.functions.FunctionBuilder;
import com.dfsek.terra.addons.terrascript.script.functions.RandomFunction;


public class RandomFunctionBuilder implements FunctionBuilder<RandomFunction> {
    @SuppressWarnings("unchecked")
    @Override
    public RandomFunction build(List<Expression<?>> argumentList, SourcePosition position) {
        return new RandomFunction((Expression<Number>) argumentList.get(0), position);
    }
    
    @Override
    public int argNumber() {
        return 1;
    }
    
    @Override
    public Expression.ReturnType getArgument(int position) {
        if(position == 0) return Expression.ReturnType.NUMBER;
        return null;
    }
}
