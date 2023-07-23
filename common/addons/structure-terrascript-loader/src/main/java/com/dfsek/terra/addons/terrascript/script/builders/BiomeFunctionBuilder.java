/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.script.builders;

import java.util.List;

import com.dfsek.terra.addons.terrascript.parser.lang.Expression;
import com.dfsek.terra.addons.terrascript.parser.lang.functions.FunctionBuilder;
import com.dfsek.terra.addons.terrascript.script.functions.BiomeFunction;
import com.dfsek.terra.addons.terrascript.tokenizer.SourcePosition;
import com.dfsek.terra.api.Platform;


public class BiomeFunctionBuilder implements FunctionBuilder<BiomeFunction> {
    private final Platform platform;
    
    public BiomeFunctionBuilder(Platform platform) {
        this.platform = platform;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public BiomeFunction build(List<Expression<?>> argumentList, SourcePosition position) {
        return new BiomeFunction((Expression<Number>) argumentList.get(0), (Expression<Number>) argumentList.get(1),
                                 (Expression<Number>) argumentList.get(2), position);
    }
    
    @Override
    public int argNumber() {
        return 3;
    }
    
    @Override
    public Expression.ReturnType getArgument(int position) {
        return switch(position) {
            case 0, 1, 2 -> Expression.ReturnType.NUMBER;
            default -> null;
        };
    }
}
