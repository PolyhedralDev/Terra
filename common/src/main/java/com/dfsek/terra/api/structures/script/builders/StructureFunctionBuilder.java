package com.dfsek.terra.api.structures.script.builders;

import com.dfsek.terra.api.platform.TerraPlugin;
import com.dfsek.terra.api.structures.parser.exceptions.ParseException;
import com.dfsek.terra.api.structures.parser.lang.Returnable;
import com.dfsek.terra.api.structures.parser.lang.functions.FunctionBuilder;
import com.dfsek.terra.api.structures.script.functions.StructureFunction;
import com.dfsek.terra.api.structures.tokenizer.Position;
import com.dfsek.terra.registry.ScriptRegistry;

import java.util.List;
import java.util.stream.Collectors;

public class StructureFunctionBuilder implements FunctionBuilder<StructureFunction> {
    private final ScriptRegistry registry;
    private final TerraPlugin main;

    public StructureFunctionBuilder(ScriptRegistry registry, TerraPlugin main) {
        this.registry = registry;
        this.main = main;
    }

    @SuppressWarnings("unchecked")
    @Override
    public StructureFunction build(List<Returnable<?>> argumentList, Position position) throws ParseException {
        if(argumentList.size() < 5) throw new ParseException("Expected rotations", position);

        return new StructureFunction((Returnable<Number>) argumentList.remove(0), (Returnable<Number>) argumentList.remove(0), (Returnable<Number>) argumentList.remove(0), (Returnable<String>) argumentList.remove(0),
                argumentList.stream().map(item -> ((Returnable<String>) item)).collect(Collectors.toList()), registry, position, main);
    }

    @Override
    public int argNumber() {
        return -1;
    }

    @Override
    public Returnable.ReturnType getArgument(int position) {
        switch(position) {
            case 0:
            case 1:
            case 2:
                return Returnable.ReturnType.NUMBER;
            default:
                return Returnable.ReturnType.STRING;
        }
    }
}
