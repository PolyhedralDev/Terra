package com.dfsek.terra.api.structures.script.builders;

import com.dfsek.terra.api.platform.TerraPlugin;
import com.dfsek.terra.api.structures.parser.FunctionBuilder;
import com.dfsek.terra.api.structures.parser.exceptions.ParseException;
import com.dfsek.terra.api.structures.script.functions.BlockFunction;

import java.util.List;

public class BlockFunctionBuilder implements FunctionBuilder<BlockFunction> {
    private final TerraPlugin main;

    public BlockFunctionBuilder(TerraPlugin main) {
        this.main = main;
    }

    @Override
    public BlockFunction build(List<String> argumentList) throws ParseException {
        return new BlockFunction(Integer.parseInt(argumentList.get(0)), Integer.parseInt(argumentList.get(1)), Integer.parseInt(argumentList.get(2)), main.getWorldHandle().createBlockData(argumentList.get(3)));
    }

    @Override
    public int getArguments() {
        return 4;
    }
}
