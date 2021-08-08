package com.dfsek.terra.addons.terrascript.builders;

import com.dfsek.terra.addons.terrascript.api.FunctionBuilder;
import com.dfsek.terra.addons.terrascript.api.Position;
import com.dfsek.terra.addons.terrascript.api.exception.ParseException;
import com.dfsek.terra.addons.terrascript.api.lang.Returnable;
import com.dfsek.terra.addons.terrascript.functions.StructureFunction;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.registry.Registry;
import com.dfsek.terra.api.structure.Structure;

import java.util.List;
import java.util.stream.Collectors;

public class StructureFunctionBuilder implements FunctionBuilder<StructureFunction> {
    private final Registry<Structure> registry;
    private final TerraPlugin main;

    public StructureFunctionBuilder(Registry<Structure> registry, TerraPlugin main) {
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
