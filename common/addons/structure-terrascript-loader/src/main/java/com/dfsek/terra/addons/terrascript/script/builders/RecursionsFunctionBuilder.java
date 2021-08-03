package com.dfsek.terra.addons.terrascript.script.builders;

import com.dfsek.terra.addons.terrascript.api.FunctionBuilder;
import com.dfsek.terra.addons.terrascript.api.Position;
import com.dfsek.terra.addons.terrascript.api.exception.ParseException;
import com.dfsek.terra.addons.terrascript.api.lang.Returnable;
import com.dfsek.terra.addons.terrascript.script.functions.RecursionsFunction;

import java.util.List;

public class RecursionsFunctionBuilder implements FunctionBuilder<RecursionsFunction> {
    @Override
    public RecursionsFunction build(List<Returnable<?>> argumentList, Position position) throws ParseException {
        return new RecursionsFunction(position);
    }

    @Override
    public int argNumber() {
        return 0;
    }

    @Override
    public Returnable.ReturnType getArgument(int position) {
        return null;
    }
}
