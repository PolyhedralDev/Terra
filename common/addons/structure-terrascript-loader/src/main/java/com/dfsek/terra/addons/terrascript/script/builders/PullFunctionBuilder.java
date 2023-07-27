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
import com.dfsek.terra.addons.terrascript.script.functions.PullFunction;
import com.dfsek.terra.api.Platform;


public class PullFunctionBuilder implements FunctionBuilder<PullFunction> {
    private final Platform platform;
    
    public PullFunctionBuilder(Platform platform) {
        this.platform = platform;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public PullFunction build(List<Expression<?>> argumentList, SourcePosition position) {
        return new PullFunction((Expression<Number>) argumentList.get(0), (Expression<Number>) argumentList.get(1),
                                (Expression<Number>) argumentList.get(2), (Expression<String>) argumentList.get(3), platform, position);
    }
    
    @Override
    public int argNumber() {
        return 4;
    }
    
    @Override
    public Expression.ReturnType getArgument(int position) {
        return switch(position) {
            case 0, 1, 2 -> Expression.ReturnType.NUMBER;
            case 3 -> Expression.ReturnType.STRING;
            default -> null;
        };
    }
}
