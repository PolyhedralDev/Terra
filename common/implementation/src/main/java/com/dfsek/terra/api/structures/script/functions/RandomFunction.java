package com.dfsek.terra.api.structures.script.functions;

import com.dfsek.terra.api.structures.parser.lang.ImplementationArguments;
import com.dfsek.terra.api.structures.parser.lang.Returnable;
import com.dfsek.terra.api.structures.parser.lang.functions.Function;
import com.dfsek.terra.api.structures.parser.lang.variables.Variable;
import com.dfsek.terra.api.structures.script.TerraImplementationArguments;
import com.dfsek.terra.api.structures.tokenizer.Position;

import java.util.Map;

public class RandomFunction implements Function<Integer> {
    private final Returnable<Number> numberReturnable;
    private final Position position;

    public RandomFunction(Returnable<Number> numberReturnable, Position position) {
        this.numberReturnable = numberReturnable;
        this.position = position;
    }


    @Override
    public ReturnType returnType() {
        return ReturnType.NUMBER;
    }

    @Override
    public Integer apply(ImplementationArguments implementationArguments, Map<String, Variable<?>> variableMap) {
        return ((TerraImplementationArguments) implementationArguments).getRandom().nextInt(numberReturnable.apply(implementationArguments, variableMap).intValue());
    }

    @Override
    public Position getPosition() {
        return position;
    }
}
