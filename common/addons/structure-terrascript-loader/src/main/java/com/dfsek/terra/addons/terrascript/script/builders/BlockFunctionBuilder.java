package com.dfsek.terra.addons.terrascript.script.builders;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.addons.terrascript.parser.exceptions.ParseException;
import com.dfsek.terra.addons.terrascript.parser.lang.Returnable;
import com.dfsek.terra.addons.terrascript.parser.lang.constants.BooleanConstant;
import com.dfsek.terra.addons.terrascript.parser.lang.functions.FunctionBuilder;
import com.dfsek.terra.addons.terrascript.script.functions.AbstractBlockFunction;
import com.dfsek.terra.addons.terrascript.script.functions.BlockFunction;
import com.dfsek.terra.addons.terrascript.script.functions.DynamicBlockFunction;
import com.dfsek.terra.addons.terrascript.tokenizer.Position;

import java.util.List;

public class BlockFunctionBuilder implements FunctionBuilder<AbstractBlockFunction> {
    private final TerraPlugin main;
    private final boolean dynamic;

    public BlockFunctionBuilder(TerraPlugin main, boolean dynamic) {
        this.main = main;
        this.dynamic = dynamic;
    }

    @SuppressWarnings("unchecked")
    @Override
    public AbstractBlockFunction build(List<Returnable<?>> argumentList, Position position) throws ParseException {
        if(argumentList.size() < 4) throw new ParseException("Expected data", position);
        Returnable<Boolean> booleanReturnable = new BooleanConstant(true, position);
        if(argumentList.size() == 5) booleanReturnable = (Returnable<Boolean>) argumentList.get(4);
        if(dynamic)
            return new DynamicBlockFunction((Returnable<Number>) argumentList.get(0), (Returnable<Number>) argumentList.get(1), (Returnable<Number>) argumentList.get(2), (Returnable<String>) argumentList.get(3), booleanReturnable, main, position);
        return new BlockFunction((Returnable<Number>) argumentList.get(0), (Returnable<Number>) argumentList.get(1), (Returnable<Number>) argumentList.get(2), (Returnable<String>) argumentList.get(3), booleanReturnable, main, position);
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
            case 3:
                return Returnable.ReturnType.STRING;
            case 4:
                return Returnable.ReturnType.BOOLEAN;
            default:
                return null;
        }
    }
}
