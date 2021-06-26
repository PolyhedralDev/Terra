package com.dfsek.terra.api.structures.script.functions;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.block.BlockState;
import com.dfsek.terra.api.structures.parser.exceptions.ParseException;
import com.dfsek.terra.api.structures.parser.lang.ImplementationArguments;
import com.dfsek.terra.api.structures.parser.lang.Returnable;
import com.dfsek.terra.api.structures.parser.lang.constants.ConstantExpression;
import com.dfsek.terra.api.structures.parser.lang.variables.Variable;
import com.dfsek.terra.api.structures.script.TerraImplementationArguments;
import com.dfsek.terra.api.structures.tokenizer.Position;

import java.util.Map;

public class BlockFunction extends AbstractBlockFunction {
    private final BlockState data;

    public BlockFunction(Returnable<Number> x, Returnable<Number> y, Returnable<Number> z, Returnable<String> data, Returnable<Boolean> overwrite, TerraPlugin main, Position position) throws ParseException {
        super(x, y, z, data, overwrite, main, position);

        if(!(data instanceof ConstantExpression)) throw new ParseException("Block data must be constant", data.getPosition());
        try {
            this.data = main.getWorldHandle().createBlockData(((ConstantExpression<String>) data).getConstant());
        } catch(IllegalArgumentException e) {
            throw new ParseException("Could not parse block data", data.getPosition(), e);
        }
    }

    @Override
    public Void apply(ImplementationArguments implementationArguments, Map<String, Variable<?>> variableMap) {
        TerraImplementationArguments arguments = (TerraImplementationArguments) implementationArguments;
        BlockState rot = data.clone();
        setBlock(implementationArguments, variableMap, arguments, rot);
        return null;
    }
}
