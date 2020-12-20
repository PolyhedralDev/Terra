package com.dfsek.terra.api.structures.script.builders;

import com.dfsek.terra.api.platform.TerraPlugin;
import com.dfsek.terra.api.structures.parser.FunctionBuilder;
import com.dfsek.terra.api.structures.parser.exceptions.ParseException;
import com.dfsek.terra.api.structures.script.functions.CheckFunction;

import java.util.List;

public class SpawnCheckBuilder implements FunctionBuilder<CheckFunction> {
    private final TerraPlugin main;

    public SpawnCheckBuilder(TerraPlugin main) {
        this.main = main;
    }

    @Override
    public CheckFunction build(List<String> argumentList) throws ParseException {
        return new CheckFunction(main, Integer.parseInt(argumentList.get(0)), Integer.parseInt(argumentList.get(1)), Integer.parseInt(argumentList.get(2)));
    }

    @Override
    public int getArguments() {
        return 3;
    }
}
