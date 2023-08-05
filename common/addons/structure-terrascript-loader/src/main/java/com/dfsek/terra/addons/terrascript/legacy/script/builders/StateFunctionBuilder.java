/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.legacy.script.builders;

import java.util.List;

import com.dfsek.terra.addons.terrascript.Type;
import com.dfsek.terra.addons.terrascript.legacy.parser.exceptions.ParseException;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.Expression;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.functions.FunctionBuilder;
import com.dfsek.terra.addons.terrascript.legacy.script.functions.StateFunction;
import com.dfsek.terra.addons.terrascript.lexer.SourcePosition;
import com.dfsek.terra.api.Platform;


public class StateFunctionBuilder implements FunctionBuilder<StateFunction> {
    private final Platform platform;
    
    public StateFunctionBuilder(Platform platform) {
        this.platform = platform;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public StateFunction build(List<Expression<?>> argumentList, SourcePosition position) {
        if(argumentList.size() < 4) throw new ParseException("Expected data", position);
        return new StateFunction((Expression<Number>) argumentList.get(0), (Expression<Number>) argumentList.get(1),
                                 (Expression<Number>) argumentList.get(2), (Expression<String>) argumentList.get(3), position);
    }
    
    @Override
    public int argNumber() {
        return 4;
    }
    
    @Override
    public Type getArgument(int position) {
        return switch(position) {
            case 0, 1, 2 -> Type.NUMBER;
            case 3 -> Type.STRING;
            default -> null;
        };
    }
}
