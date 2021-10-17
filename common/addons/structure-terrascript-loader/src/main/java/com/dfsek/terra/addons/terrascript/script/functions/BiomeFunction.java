package com.dfsek.terra.addons.terrascript.script.functions;

import com.dfsek.terra.api.Platform;

import net.jafama.FastMath;

import java.util.Map;

import com.dfsek.terra.addons.terrascript.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.parser.lang.Returnable;
import com.dfsek.terra.addons.terrascript.parser.lang.functions.Function;
import com.dfsek.terra.addons.terrascript.parser.lang.variables.Variable;
import com.dfsek.terra.addons.terrascript.script.TerraImplementationArguments;
import com.dfsek.terra.addons.terrascript.tokenizer.Position;
import com.dfsek.terra.api.util.RotationUtil;
import com.dfsek.terra.api.util.vector.Vector2;
import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;


public class BiomeFunction implements Function<String> {
    private final Platform platform;
    private final Returnable<Number> x, y, z;
    private final Position position;
    
    
    public BiomeFunction(Platform platform, Returnable<Number> x, Returnable<Number> y, Returnable<Number> z, Position position) {
        this.platform = platform;
        this.x = x;
        this.y = y;
        this.z = z;
        this.position = position;
    }
    
    
    @Override
    public String apply(ImplementationArguments implementationArguments, Map<String, Variable<?>> variableMap) {
        TerraImplementationArguments arguments = (TerraImplementationArguments) implementationArguments;
        
        Vector2 xz = new Vector2(x.apply(implementationArguments, variableMap).doubleValue(),
                                 z.apply(implementationArguments, variableMap).doubleValue());
        
        RotationUtil.rotateVector(xz, arguments.getRotation());
        
        BiomeProvider grid = arguments.getWorld().getBiomeProvider();
        
        return grid.getBiome(arguments.getBuffer()
                                      .getOrigin()
                                      .clone()
                                      .add(new Vector3(FastMath.roundToInt(xz.getX()),
                                                       y.apply(implementationArguments, variableMap).intValue(),
                                                       FastMath.roundToInt(xz.getZ()))), arguments.getWorld().getSeed()).getID();
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
