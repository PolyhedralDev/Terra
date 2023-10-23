/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.script.builders;

import java.util.List;

import com.dfsek.terra.addons.terrascript.parser.lang.Returnable;
import com.dfsek.terra.addons.terrascript.parser.lang.functions.FunctionBuilder;
import com.dfsek.terra.addons.terrascript.script.functions.SetMarkFunction;
import com.dfsek.terra.addons.terrascript.tokenizer.Position;


public class SetMarkFunctionBuilder implements FunctionBuilder<SetMarkFunction> {
    
    public SetMarkFunctionBuilder() {
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public SetMarkFunction build(List<Returnable<?>> argumentList, Position position) {
        return new SetMarkFunction((Returnable<Number>) argumentList.get(0), (Returnable<Number>) argumentList.get(1),
                                   (Returnable<Number>) argumentList.get(2), (Returnable<String>) argumentList.get(3), position);
    }
    
    @Override
    public int argNumber() {
        return 4;
    }
    
    @Override
    public Returnable.ReturnType getArgument(int position) {
        return switch(position) {
            case 0, 1, 2 -> Returnable.ReturnType.NUMBER;
            case 3 -> Returnable.ReturnType.STRING;
            default -> null;
        };
    }
}
