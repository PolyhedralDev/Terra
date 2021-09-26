package com.dfsek.terra.addons.terrascript.script.builders;

import java.util.List;

import com.dfsek.terra.addons.terrascript.parser.exceptions.ParseException;
import com.dfsek.terra.addons.terrascript.parser.lang.Returnable;
import com.dfsek.terra.addons.terrascript.parser.lang.constants.BooleanConstant;
import com.dfsek.terra.addons.terrascript.parser.lang.constants.StringConstant;
import com.dfsek.terra.addons.terrascript.parser.lang.functions.FunctionBuilder;
import com.dfsek.terra.addons.terrascript.script.functions.BlockFunction;
import com.dfsek.terra.addons.terrascript.tokenizer.Position;
import com.dfsek.terra.api.Platform;


public class BlockFunctionBuilder implements FunctionBuilder<BlockFunction> {
    private final Platform main;
    
    public BlockFunctionBuilder(Platform main) {
        this.main = main;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public BlockFunction build(List<Returnable<?>> argumentList, Position position) throws ParseException {
        if(argumentList.size() < 4) throw new ParseException("Expected data", position);
        Returnable<Boolean> booleanReturnable = new BooleanConstant(true, position);
        if(argumentList.size() == 5) booleanReturnable = (Returnable<Boolean>) argumentList.get(4);
        if(argumentList.get(3) instanceof StringConstant) {
            return new BlockFunction.Constant((Returnable<Number>) argumentList.get(0), (Returnable<Number>) argumentList.get(1),
                                              (Returnable<Number>) argumentList.get(2), (StringConstant) argumentList.get(3),
                                              booleanReturnable, main, position);
        }
        return new BlockFunction((Returnable<Number>) argumentList.get(0), (Returnable<Number>) argumentList.get(1),
                                 (Returnable<Number>) argumentList.get(2), (Returnable<String>) argumentList.get(3), booleanReturnable,
                                 main, position);
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
