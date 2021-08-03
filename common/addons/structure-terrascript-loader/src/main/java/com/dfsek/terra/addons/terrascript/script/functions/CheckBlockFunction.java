package com.dfsek.terra.addons.terrascript.script.functions;

import com.dfsek.terra.addons.terrascript.api.Function;
import com.dfsek.terra.addons.terrascript.api.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.api.Position;
import com.dfsek.terra.addons.terrascript.api.lang.Returnable;
import com.dfsek.terra.addons.terrascript.api.lang.Variable;
import com.dfsek.terra.addons.terrascript.script.TerraImplementationArguments;
import com.dfsek.terra.api.util.RotationUtil;
import com.dfsek.terra.api.vector.Vector2;
import com.dfsek.terra.api.vector.Vector3;
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

        Vector2 xz = new Vector2(x.apply(implementationArguments, variableMap).doubleValue(), z.apply(implementationArguments, variableMap).doubleValue());

        RotationUtil.rotateVector(xz, arguments.getRotation());

        String data = arguments.getWorld().getBlockData(arguments.getBuffer().getOrigin().clone().add(new Vector3(FastMath.roundToInt(xz.getX()), y.apply(implementationArguments, variableMap).doubleValue(), FastMath.roundToInt(xz.getZ())))).getAsString();
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
