package com.dfsek.terra.api.structures.script.builders;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.structures.parser.lang.Returnable;
import com.dfsek.terra.api.structures.parser.lang.functions.FunctionBuilder;
import com.dfsek.terra.api.structures.script.StructureScript;
import com.dfsek.terra.api.structures.script.functions.LootFunction;
import com.dfsek.terra.api.structures.tokenizer.Position;
import com.dfsek.terra.registry.config.LootRegistry;

import java.util.List;

public class LootFunctionBuilder implements FunctionBuilder<LootFunction> {
    private final TerraPlugin main;
    private final LootRegistry registry;
    private final StructureScript script;

    public LootFunctionBuilder(TerraPlugin main, LootRegistry registry, StructureScript script) {
        this.main = main;
        this.registry = registry;
        this.script = script;
    }

    @SuppressWarnings("unchecked")
    @Override
    public LootFunction build(List<Returnable<?>> argumentList, Position position) {
        return new LootFunction(registry, (Returnable<Number>) argumentList.get(0), (Returnable<Number>) argumentList.get(1), (Returnable<Number>) argumentList.get(2), (Returnable<String>) argumentList.get(3), main, position, script);
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
