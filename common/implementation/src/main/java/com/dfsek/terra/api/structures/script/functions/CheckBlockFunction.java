package com.dfsek.terra.api.structures.script.functions;

import com.dfsek.terra.api.structures.parser.lang.ImplementationArguments;
import com.dfsek.terra.api.structures.parser.lang.Returnable;
import com.dfsek.terra.api.structures.parser.lang.functions.Function;
import com.dfsek.terra.api.structures.parser.lang.variables.Variable;
import com.dfsek.terra.api.structures.script.TerraImplementationArguments;
import com.dfsek.terra.api.structures.tokenizer.Position;
import com.dfsek.terra.api.util.RotationUtil;
import com.dfsek.terra.api.vector.Vector2;
import com.dfsek.terra.vector.Vector2Impl;
import com.dfsek.terra.vector.Vector3Impl;
import net.jafama.FastMath;

import java.util.Map;

public class CheckBlockFunction implements Function<String> {
    private final Returnable<Number> x, y, z;
    private final Position position;

    public CheckBlockFunction(Returnable<Number> x, Returnable<Number> y, Returnable<Number> z, Position position) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.position = position;
    }


    @Override
    public String apply(ImplementationArguments implementationArguments, Map<String, Variable<?>> variableMap) {
        TerraImplementationArguments arguments = (TerraImplementationArguments) implementationArguments;

        Vector2 xz = new Vector2Impl(x.apply(implementationArguments, variableMap).doubleValue(), z.apply(implementationArguments, variableMap).doubleValue());

        RotationUtil.rotateVector(xz, arguments.getRotation());

        String data  = arguments.getWorld().getBlockData(arguments.getBuffer().getOrigin().clone().add(new Vector3Impl(FastMath.roundToInt(xz.getX()), y.apply(implementationArguments, variableMap).doubleValue(), FastMath.roundToInt(xz.getZ())))).getAsString();
        if(data.contains("[")) return data.substring(0, data.indexOf('[')); // Strip properties
        else return data;
    }


    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public ReturnType returnType() {
        return ReturnType.STRING;
    }
}
