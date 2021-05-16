package com.dfsek.terra.api.structures.script.builders;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.structures.parser.exceptions.ParseException;
import com.dfsek.terra.api.structures.parser.lang.Returnable;
import com.dfsek.terra.api.structures.parser.lang.constants.BooleanConstant;
import com.dfsek.terra.api.structures.parser.lang.functions.FunctionBuilder;
import com.dfsek.terra.api.structures.script.functions.AbstractBlockFunction;
import com.dfsek.terra.api.structures.script.functions.BlockFunction;
import com.dfsek.terra.api.structures.script.functions.DynamicBlockFunction;
import com.dfsek.terra.api.structures.tokenizer.Position;

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
        return switch(position) {
            case 0, 1, 2 -> Returnable.ReturnType.NUMBER;
            case 3 -> Returnable.ReturnType.STRING;
            case 4 -> Returnable.ReturnType.BOOLEAN;
            default -> null;
        };
    }
}
