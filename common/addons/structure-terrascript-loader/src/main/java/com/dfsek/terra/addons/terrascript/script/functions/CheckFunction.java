package com.dfsek.terra.addons.terrascript.script.functions;

import com.dfsek.terra.addons.terrascript.api.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.api.lang.Returnable;
import com.dfsek.terra.addons.terrascript.api.Function;
import com.dfsek.terra.addons.terrascript.api.lang.Variable;
import com.dfsek.terra.addons.terrascript.script.TerraImplementationArguments;
import com.dfsek.terra.addons.terrascript.api.Position;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.util.RotationUtil;
import com.dfsek.terra.api.vector.Vector2;
import com.dfsek.terra.api.vector.Vector3;
import com.dfsek.terra.api.world.World;
import com.dfsek.terra.api.world.generator.SamplerCache;
import net.jafama.FastMath;

import java.util.Map;

public class CheckFunction implements Function<String> {
    private final TerraPlugin main;
    private final Returnable<Number> x, y, z;
    private final Position position;

    public CheckFunction(TerraPlugin main, Returnable<Number> x, Returnable<Number> y, Returnable<Number> z, Position position) {
        this.main = main;
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

        Vector3 location = arguments.getBuffer().getOrigin().clone().add(new Vector3(FastMath.roundToInt(xz.getX()), y.apply(implementationArguments, variableMap).doubleValue(), FastMath.roundToInt(xz.getZ())));

        return apply(location, arguments.getWorld());
    }

    private String apply(Vector3 vector, World world) {
        int y = vector.getBlockY();
        if(y >= world.getMaxHeight() || y < 0) return "AIR";
        SamplerCache cache = world.getConfig().getSamplerCache();
        double comp = sample(vector.getBlockX(), vector.getBlockY(), vector.getBlockZ(), cache);

        if(comp > 0) return "LAND"; // If noise val is greater than zero, location will always be land.

        //BiomeProvider provider = tw.getBiomeProvider();
        //TerraBiome b = provider.getBiome(vector.getBlockX(), vector.getBlockZ());

        /*if(vector.getY() > c.getSeaLevel())*/ return "AIR"; // Above sea level
        //return "OCEAN"; // Below sea level
    }

    private double sample(int x, int y, int z, SamplerCache cache) {
        int cx = FastMath.floorDiv(x, 16);
        int cz = FastMath.floorDiv(z, 16);
        return cache.get(x, z).sample(x - (cx << 4), y, z - (cz << 4));
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
