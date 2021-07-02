package com.dfsek.terra.addons.structure.structures.script.builders;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.addons.structure.structures.parser.exceptions.ParseException;
import com.dfsek.terra.addons.structure.structures.parser.lang.Returnable;
import com.dfsek.terra.addons.structure.structures.parser.lang.functions.FunctionBuilder;
import com.dfsek.terra.addons.structure.structures.script.functions.StateFunction;
import com.dfsek.terra.addons.structure.structures.tokenizer.Position;

import java.util.List;

public class StateFunctionBuilder implements FunctionBuilder<StateFunction> {
    private final TerraPlugin main;

    public StateFunctionBuilder(TerraPlugin main) {
        this.main = main;
    }

    @SuppressWarnings("unchecked")
    @Override
    public StateFunction build(List<Returnable<?>> argumentList, Position position) throws ParseException {
        if(argumentList.size() < 4) throw new ParseException("Expected data", position);
        return new StateFunction((Returnable<Number>) argumentList.get(0), (Returnable<Number>) argumentList.get(1), (Returnable<Number>) argumentList.get(2), (Returnable<String>) argumentList.get(3), main, position);
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
