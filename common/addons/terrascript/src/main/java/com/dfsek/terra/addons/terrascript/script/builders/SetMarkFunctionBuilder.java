package com.dfsek.terra.addons.terrascript.script.builders;

import com.dfsek.terra.addons.terrascript.parser.exceptions.ParseException;
import com.dfsek.terra.addons.terrascript.parser.lang.Returnable;
import com.dfsek.terra.addons.terrascript.parser.lang.functions.FunctionBuilder;
import com.dfsek.terra.addons.terrascript.script.functions.SetMarkFunction;
import com.dfsek.terra.addons.terrascript.tokenizer.Position;

import java.util.List;

public class SetMarkFunctionBuilder implements FunctionBuilder<SetMarkFunction> {

    public SetMarkFunctionBuilder() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public SetMarkFunction build(List<Returnable<?>> argumentList, Position position) throws ParseException {
        return new SetMarkFunction((Returnable<Number>) argumentList.get(0), (Returnable<Number>) argumentList.get(1), (Returnable<Number>) argumentList.get(2), (Returnable<String>) argumentList.get(3), position);
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
