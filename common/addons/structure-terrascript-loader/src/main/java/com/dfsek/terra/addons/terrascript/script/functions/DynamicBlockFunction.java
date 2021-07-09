package com.dfsek.terra.addons.terrascript.script.functions;

import com.dfsek.terra.addons.terrascript.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.parser.lang.Returnable;
import com.dfsek.terra.addons.terrascript.parser.lang.variables.Variable;
import com.dfsek.terra.addons.terrascript.script.TerraImplementationArguments;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.addons.terrascript.tokenizer.Position;

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
