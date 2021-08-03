package com.dfsek.terra.addons.terrascript.script.builders;

import com.dfsek.terra.addons.terrascript.api.Returnable;
import com.dfsek.terra.addons.terrascript.api.FunctionBuilder;
import com.dfsek.terra.addons.terrascript.script.functions.GetMarkFunction;
import com.dfsek.terra.addons.terrascript.api.Position;

import java.util.List;

public class GetMarkFunctionBuilder implements FunctionBuilder<GetMarkFunction> {

    public GetMarkFunctionBuilder() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public GetMarkFunction build(List<Returnable<?>> argumentList, Position position) {
        return new GetMarkFunction((Returnable<Number>) argumentList.get(0), (Returnable<Number>) argumentList.get(1), (Returnable<Number>) argumentList.get(2), position);
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
