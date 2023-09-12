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
import com.dfsek.terra.addons.terrascript.legacy.script.functions.GetMarkFunction;
import com.dfsek.terra.addons.terrascript.lexer.SourcePosition;


public class GetMarkFunctionBuilder implements FunctionBuilder<GetMarkFunction> {
    
    public GetMarkFunctionBuilder() {
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public GetMarkFunction build(List<Expression<?>> argumentList, SourcePosition position) {
        return new GetMarkFunction((Expression<Number>) argumentList.get(0), (Expression<Number>) argumentList.get(1),
                                   (Expression<Number>) argumentList.get(2), position);
    }
    
    @Override
    public int argNumber() {
        return 3;
    }
    
    @Override
    public Type getArgument(int position) {
        return switch(position) {
            case 0, 1, 2 -> Type.NUMBER;
            default -> null;
        };
    }
}