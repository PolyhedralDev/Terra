package com.dfsek.terra.api.structures.script.builders;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.structures.parser.exceptions.ParseException;
import com.dfsek.terra.api.structures.parser.lang.Returnable;
import com.dfsek.terra.api.structures.parser.lang.functions.FunctionBuilder;
import com.dfsek.terra.api.structures.script.functions.CheckFunction;
import com.dfsek.terra.api.structures.tokenizer.Position;
import com.dfsek.terra.world.generation.math.SamplerCache;

import java.util.List;

public class CheckFunctionBuilder implements FunctionBuilder<CheckFunction> {
    private final TerraPlugin main;
    private final SamplerCache cache;

    public CheckFunctionBuilder(TerraPlugin main, SamplerCache cache) {
        this.main = main;
        this.cache = cache;
    }

    @SuppressWarnings("unchecked")
    @Override
    public CheckFunction build(List<Returnable<?>> argumentList, Position position) throws ParseException {
        return new CheckFunction(main, (Returnable<Number>) argumentList.get(0), (Returnable<Number>) argumentList.get(1), (Returnable<Number>) argumentList.get(2), cache, position);
    }

    @Override
    public int argNumber() {
        return 3;
    }

    @Override
    public Returnable.ReturnType getArgument(int position) {
        switch(position) {
            case 0:
            case 1:
            case 2:
                return Returnable.ReturnType.NUMBER;
            default:
                return null;
        }
    }
}
