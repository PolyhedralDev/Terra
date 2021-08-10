package com.dfsek.terra.addons.terrascript.functions;

import com.dfsek.terra.addons.terrascript.api.Function;
import com.dfsek.terra.addons.terrascript.api.Position;
import com.dfsek.terra.addons.terrascript.api.TerraProperties;
import com.dfsek.terra.addons.terrascript.api.lang.Returnable;
import com.dfsek.terra.addons.terrascript.api.lang.Variable;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.properties.Context;
import com.dfsek.terra.api.util.RotationUtil;
import com.dfsek.terra.api.vector.Vector2;
import com.dfsek.terra.api.vector.Vector3;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import net.jafama.FastMath;

import java.util.Map;

public class BiomeFunction implements Function<String> {
    private final TerraPlugin main;
    private final Returnable<Number> x, y, z;
    private final Position position;


    public BiomeFunction(TerraPlugin main, Returnable<Number> x, Returnable<Number> y, Returnable<Number> z, Position position) {
        this.main = main;
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

        BiomeProvider grid = properties.getWorld().getBiomeProvider();

        return grid.getBiome(properties.getBuffer().getOrigin().clone().add(new Vector3(FastMath.roundToInt(xz.getX()), y.apply(context, variableMap).intValue(), FastMath.roundToInt(xz.getZ()))), properties.getWorld().getSeed()).getID();
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
