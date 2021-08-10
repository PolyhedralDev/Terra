package com.dfsek.terra.addons.terrascript.functions;

import com.dfsek.terra.addons.terrascript.api.Function;
import com.dfsek.terra.addons.terrascript.api.Position;
import com.dfsek.terra.addons.terrascript.api.lang.Returnable;
import com.dfsek.terra.addons.terrascript.api.lang.Variable;
import com.dfsek.terra.addons.terrascript.api.TerraProperties;
import com.dfsek.terra.api.properties.Context;
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
    public String apply(Context context, Map<String, Variable<?>> variableMap) {
        TerraProperties properties = context.get(TerraProperties.class);

        Vector2 xz = new Vector2(x.apply(context, variableMap).doubleValue(), z.apply(context, variableMap).doubleValue());

        RotationUtil.rotateVector(xz, properties.getRotation());

        String data = properties.getWorld().getBlockData(properties.getBuffer().getOrigin().clone().add(new Vector3(FastMath.roundToInt(xz.getX()), y.apply(context, variableMap).doubleValue(), FastMath.roundToInt(xz.getZ())))).getAsString();
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
