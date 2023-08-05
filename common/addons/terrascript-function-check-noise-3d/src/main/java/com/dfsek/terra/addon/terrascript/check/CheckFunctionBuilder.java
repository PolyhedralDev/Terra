/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addon.terrascript.check;

import java.util.List;

import com.dfsek.terra.addons.terrascript.Type;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.Expression;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.functions.FunctionBuilder;
import com.dfsek.terra.addons.terrascript.lexer.SourcePosition;
import com.dfsek.terra.api.Platform;


public class CheckFunctionBuilder implements FunctionBuilder<CheckFunction> {
    private final Platform platform;
    
    public CheckFunctionBuilder(Platform platform) {
        this.platform = platform;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public CheckFunction build(List<Expression<?>> argumentList, SourcePosition position) {
        return new CheckFunction((Expression<Number>) argumentList.get(0), (Expression<Number>) argumentList.get(1),
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
