/*
 * Copyright (c) 2020-2023 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.script.builders;

import java.util.List;

import com.dfsek.terra.addons.terrascript.parser.exceptions.ParseException;
import com.dfsek.terra.addons.terrascript.parser.lang.Returnable;
import com.dfsek.terra.addons.terrascript.parser.lang.constants.BooleanConstant;
import com.dfsek.terra.addons.terrascript.parser.lang.constants.StringConstant;
import com.dfsek.terra.addons.terrascript.parser.lang.functions.FunctionBuilder;
import com.dfsek.terra.addons.terrascript.script.functions.BlockFunction;
import com.dfsek.terra.addons.terrascript.tokenizer.Position;
import com.dfsek.terra.api.Platform;


public class BlockFunctionBuilder implements FunctionBuilder<BlockFunction> {
    private final Platform platform;
    
    public BlockFunctionBuilder(Platform platform) {
        this.platform = platform;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public BlockFunction build(List<Returnable<?>> argumentList, Position position) {
        if(argumentList.size() < 4) throw new ParseException("Expected data", position);
        Returnable<Boolean> overwrite = new BooleanConstant(true, position);
        if(argumentList.size() >= 5) overwrite = (Returnable<Boolean>) argumentList.get(4);
        Returnable<Boolean> physics = new BooleanConstant(false, position);
        if(argumentList.size() == 6) physics = (Returnable<Boolean>) argumentList.get(5);
        if(argumentList.get(3) instanceof StringConstant) {
            return new BlockFunction.Constant((Returnable<Number>) argumentList.get(0), (Returnable<Number>) argumentList.get(1),
                                              (Returnable<Number>) argumentList.get(2), (StringConstant) argumentList.get(3),
                                              overwrite, physics, platform, position);
        }
        return new BlockFunction((Returnable<Number>) argumentList.get(0), (Returnable<Number>) argumentList.get(1),
                                 (Returnable<Number>) argumentList.get(2), (Returnable<String>) argumentList.get(3), overwrite, physics,
                                 platform, position);
    }
    
    @Override
    public int argNumber() {
        return -1;
    }
    
    @Override
    public Returnable.ReturnType getArgument(int position) {
        return switch(position) {
            case 0, 1, 2 -> Returnable.ReturnType.NUMBER;
            case 3 -> Returnable.ReturnType.STRING;
            case 4, 5 -> Returnable.ReturnType.BOOLEAN;
            default -> null;
        };
    }
}
