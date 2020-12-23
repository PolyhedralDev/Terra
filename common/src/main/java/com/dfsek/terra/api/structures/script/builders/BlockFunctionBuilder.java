package com.dfsek.terra.api.structures.script.builders;

import com.dfsek.terra.api.platform.TerraPlugin;
import com.dfsek.terra.api.structures.parser.exceptions.ParseException;
import com.dfsek.terra.api.structures.parser.lang.Returnable;
import com.dfsek.terra.api.structures.parser.lang.functions.FunctionBuilder;
import com.dfsek.terra.api.structures.script.functions.BlockFunction;
import com.dfsek.terra.api.structures.tokenizer.Position;

import java.util.List;

public class BlockFunctionBuilder implements FunctionBuilder<BlockFunction> {
    private final TerraPlugin main;

    public BlockFunctionBuilder(TerraPlugin main) {
        this.main = main;
    }

    @SuppressWarnings("unchecked")
    @Override
    public BlockFunction build(List<Returnable<?>> argumentList, Position position) throws ParseException {
        return new BlockFunction((Returnable<Number>) argumentList.get(0), (Returnable<Number>) argumentList.get(1), (Returnable<Number>) argumentList.get(2), (Returnable<String>) argumentList.get(3), main, position);
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
