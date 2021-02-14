package com.dfsek.terra.api.structures.script.builders;

import com.dfsek.terra.api.core.TerraPlugin;
import com.dfsek.terra.api.structures.parser.lang.Returnable;
import com.dfsek.terra.api.structures.parser.lang.functions.FunctionBuilder;
import com.dfsek.terra.api.structures.script.functions.LootFunction;
import com.dfsek.terra.api.structures.tokenizer.Position;
import com.dfsek.terra.registry.config.LootRegistry;

import java.util.List;

public class LootFunctionBuilder implements FunctionBuilder<LootFunction> {
    private final TerraPlugin main;
    private final LootRegistry registry;

    public LootFunctionBuilder(TerraPlugin main, LootRegistry registry) {
        this.main = main;
        this.registry = registry;
    }

    @SuppressWarnings("unchecked")
    @Override
    public LootFunction build(List<Returnable<?>> argumentList, Position position) {
        return new LootFunction(registry, (Returnable<Number>) argumentList.get(0), (Returnable<Number>) argumentList.get(1), (Returnable<Number>) argumentList.get(2), (Returnable<String>) argumentList.get(3), main, position);
    }

    @Override
    public int argNumber() {
        return 4;
    }

    @Override
    public Returnable.ReturnType getArgument(int position) {
        switch(position) {
            case 0:
            case 1:
            case 2:
                return Returnable.ReturnType.NUMBER;
            case 3:
                return Returnable.ReturnType.STRING;
            default:
                return null;
        }
    }
}
