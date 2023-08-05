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
import com.dfsek.terra.addons.terrascript.legacy.script.StructureScript;
import com.dfsek.terra.addons.terrascript.legacy.script.functions.LootFunction;
import com.dfsek.terra.addons.terrascript.lexer.SourcePosition;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.registry.Registry;
import com.dfsek.terra.api.structure.LootTable;


public class LootFunctionBuilder implements FunctionBuilder<LootFunction> {
    private final Platform platform;
    private final Registry<LootTable> registry;
    private final StructureScript script;
    
    public LootFunctionBuilder(Platform platform, Registry<LootTable> registry, StructureScript script) {
        this.platform = platform;
        this.registry = registry;
        this.script = script;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public LootFunction build(List<Expression<?>> argumentList, SourcePosition position) {
        return new LootFunction(registry, (Expression<Number>) argumentList.get(0), (Expression<Number>) argumentList.get(1),
                                (Expression<Number>) argumentList.get(2), (Expression<String>) argumentList.get(3), platform, position,
                                script);
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
