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
import com.dfsek.terra.addons.terrascript.parser.lang.Returnable;
import com.dfsek.terra.addons.terrascript.parser.lang.functions.FunctionBuilder;
import com.dfsek.terra.addons.terrascript.script.functions.StructureFunction;
import com.dfsek.terra.addons.terrascript.tokenizer.Position;
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
    public StructureFunction build(List<Returnable<?>> argumentList, Position position) {
        if(argumentList.size() < 5) throw new ParseException("Expected rotations", position);
        
        return new StructureFunction((Returnable<Number>) argumentList.remove(0), (Returnable<Number>) argumentList.remove(0),
                                     (Returnable<Number>) argumentList.remove(0), (Returnable<String>) argumentList.remove(0),
                                     argumentList.stream().map(item -> ((Returnable<String>) item)).collect(Collectors.toList()), registry,
                                     position, platform);
    }
    
    @Override
    public int argNumber() {
        return -1;
    }
    
    @Override
    public Returnable.ReturnType getArgument(int position) {
        return switch(position) {
            case 0, 1, 2 -> Returnable.ReturnType.NUMBER;
            default -> Returnable.ReturnType.STRING;
        };
    }
}
