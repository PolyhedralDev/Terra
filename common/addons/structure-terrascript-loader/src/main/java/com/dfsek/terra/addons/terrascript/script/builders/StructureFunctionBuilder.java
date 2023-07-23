/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.script.builders;

import java.util.List;
import java.util.stream.Collectors;

import com.dfsek.terra.addons.terrascript.parser.exceptions.ParseException;
import com.dfsek.terra.addons.terrascript.parser.lang.Expression;
import com.dfsek.terra.addons.terrascript.parser.lang.functions.FunctionBuilder;
import com.dfsek.terra.addons.terrascript.script.functions.StructureFunction;
import com.dfsek.terra.addons.terrascript.tokenizer.SourcePosition;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.registry.Registry;
import com.dfsek.terra.api.structure.Structure;


public class StructureFunctionBuilder implements FunctionBuilder<StructureFunction> {
    private final Registry<Structure> registry;
    private final Platform platform;
    
    public StructureFunctionBuilder(Registry<Structure> registry, Platform platform) {
        this.registry = registry;
        this.platform = platform;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public StructureFunction build(List<Expression<?>> argumentList, SourcePosition position) {
        if(argumentList.size() < 5) throw new ParseException("Expected rotations", position);
        
        return new StructureFunction((Expression<Number>) argumentList.remove(0), (Expression<Number>) argumentList.remove(0),
                                     (Expression<Number>) argumentList.remove(0), (Expression<String>) argumentList.remove(0),
                                     argumentList.stream().map(item -> ((Expression<String>) item)).collect(Collectors.toList()), registry,
                                     position, platform);
    }
    
    @Override
    public int argNumber() {
        return -1;
    }
    
    @Override
    public Expression.ReturnType getArgument(int position) {
        return switch(position) {
            case 0, 1, 2 -> Expression.ReturnType.NUMBER;
            default -> Expression.ReturnType.STRING;
        };
    }
}
