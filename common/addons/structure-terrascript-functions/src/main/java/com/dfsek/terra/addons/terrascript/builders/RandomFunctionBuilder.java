package com.dfsek.terra.addons.terrascript.builders;

import com.dfsek.terra.addons.terrascript.api.FunctionBuilder;
import com.dfsek.terra.addons.terrascript.api.Position;
import com.dfsek.terra.addons.terrascript.api.exception.ParseException;
import com.dfsek.terra.addons.terrascript.api.lang.Returnable;
import com.dfsek.terra.addons.terrascript.functions.RandomFunction;

import java.util.List;

public class RandomFunctionBuilder implements FunctionBuilder<RandomFunction> {
    @SuppressWarnings("unchecked")
    @Override
    public RandomFunction build(List<Returnable<?>> argumentList, Position position) throws ParseException {
        return new RandomFunction((Returnable<Number>) argumentList.get(0), position);
    }

    @Override
    public int argNumber() {
        return 1;
    }

    @Override
    public Returnable.ReturnType getArgument(int position) {
        if(position == 0) return Returnable.ReturnType.NUMBER;
        return null;
    }
}
