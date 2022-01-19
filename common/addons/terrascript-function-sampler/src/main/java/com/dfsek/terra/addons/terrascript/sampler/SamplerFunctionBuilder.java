package com.dfsek.terra.addons.terrascript.sampler;

import com.dfsek.terra.addons.terrascript.parser.lang.Returnable;
import com.dfsek.terra.addons.terrascript.parser.lang.Returnable.ReturnType;
import com.dfsek.terra.addons.terrascript.parser.lang.functions.FunctionBuilder;
import com.dfsek.terra.addons.terrascript.tokenizer.Position;

import java.util.List;


public class SamplerFunctionBuilder implements FunctionBuilder<SamplerFunction> {
    @Override
    public SamplerFunction build(List<Returnable<?>> argumentList, Position position) {
        return null;
    }
    
    @Override
    public int argNumber() {
        return -1;
    }
    
    @Override
    public ReturnType getArgument(int position) {
        return switch(position) {
            case 0 -> ReturnType.STRING;
            case 1, 2, 3 -> ReturnType.NUMBER;
            default -> null;
        };
    }
}
