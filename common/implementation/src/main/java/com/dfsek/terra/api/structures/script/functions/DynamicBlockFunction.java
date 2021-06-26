package com.dfsek.terra.api.structures.script.functions;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.block.BlockState;
import com.dfsek.terra.api.structures.parser.lang.ImplementationArguments;
import com.dfsek.terra.api.structures.parser.lang.Returnable;
import com.dfsek.terra.api.structures.parser.lang.variables.Variable;
import com.dfsek.terra.api.structures.script.TerraImplementationArguments;
import com.dfsek.terra.api.structures.tokenizer.Position;

import java.util.HashMap;
import java.util.Map;

public class DynamicBlockFunction extends AbstractBlockFunction {
    private final Map<String, BlockState> data = new HashMap<>();
    private final Position position;


    public DynamicBlockFunction(Returnable<Number> x, Returnable<Number> y, Returnable<Number> z, Returnable<String> data, Returnable<Boolean> overwrite, TerraPlugin main, Position position) {
        super(x, y, z, data, overwrite, main, position);
        this.position = position;
    }

    @Override
    public Void apply(ImplementationArguments implementationArguments, Map<String, Variable<?>> variableMap) {
        TerraImplementationArguments arguments = (TerraImplementationArguments) implementationArguments;
        BlockState rot = data.computeIfAbsent(blockData.apply(implementationArguments, variableMap), main.getWorldHandle()::createBlockData).clone();
        setBlock(implementationArguments, variableMap, arguments, rot);
        return null;
    }
}
