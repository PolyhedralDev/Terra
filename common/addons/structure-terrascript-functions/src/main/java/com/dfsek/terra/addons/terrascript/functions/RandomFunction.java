package com.dfsek.terra.addons.terrascript.functions;

import com.dfsek.terra.addons.terrascript.api.Function;
import com.dfsek.terra.addons.terrascript.api.Position;
import com.dfsek.terra.addons.terrascript.api.lang.Returnable;
import com.dfsek.terra.addons.terrascript.api.lang.Variable;
import com.dfsek.terra.addons.terrascript.api.TerraProperties;
import com.dfsek.terra.api.properties.Context;

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
    public Integer apply(Context context, Map<String, Variable<?>> variableMap) {
        return context.get(TerraProperties.class).getRandom().nextInt(numberReturnable.apply(context, variableMap).intValue());
    }

    @Override
    public Position getPosition() {
        return position;
    }
}
