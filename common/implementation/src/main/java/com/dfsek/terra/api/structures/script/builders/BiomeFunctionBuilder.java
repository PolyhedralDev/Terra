package com.dfsek.terra.api.structures.script.builders;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.structures.parser.lang.Returnable;
import com.dfsek.terra.api.structures.parser.lang.functions.FunctionBuilder;
import com.dfsek.terra.api.structures.script.functions.BiomeFunction;
import com.dfsek.terra.api.structures.tokenizer.Position;

import java.util.List;

public class BiomeFunctionBuilder implements FunctionBuilder<BiomeFunction> {
    private final TerraPlugin main;

    public BiomeFunctionBuilder(TerraPlugin main) {
        this.main = main;
    }

    @SuppressWarnings("unchecked")
    @Override
    public BiomeFunction build(List<Returnable<?>> argumentList, Position position) {
        return new BiomeFunction(main, (Returnable<Number>) argumentList.get(0), (Returnable<Number>) argumentList.get(1), (Returnable<Number>) argumentList.get(2), position);
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
