/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addon.terrascript.check;

import net.jafama.FastMath;

import com.dfsek.terra.addons.chunkgenerator.generation.NoiseChunkGenerator3D;
import com.dfsek.terra.addons.chunkgenerator.generation.math.samplers.SamplerProvider;
import com.dfsek.terra.addons.terrascript.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.parser.lang.Returnable;
import com.dfsek.terra.addons.terrascript.parser.lang.Scope;
import com.dfsek.terra.addons.terrascript.parser.lang.functions.Function;
import com.dfsek.terra.addons.terrascript.script.TerraImplementationArguments;
import com.dfsek.terra.addons.terrascript.tokenizer.Position;
import com.dfsek.terra.api.util.RotationUtil;
import com.dfsek.terra.api.util.vector.Vector2;
import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.api.world.World;
import com.dfsek.terra.api.world.WritableWorld;


public class CheckFunction implements Function<String> {
    private final Returnable<Number> x, y, z;
    private final Position position;
    
    public CheckFunction(Returnable<Number> x, Returnable<Number> y, Returnable<Number> z, Position position) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.position = position;
    }
    
    
    @Override
    public String apply(ImplementationArguments implementationArguments, Scope scope) {
        
        
        TerraImplementationArguments arguments = (TerraImplementationArguments) implementationArguments;
        
        
        Vector2 xz = Vector2.of(x.apply(implementationArguments, scope).doubleValue(),
                                z.apply(implementationArguments, scope).doubleValue());
        
        RotationUtil.rotateVector(xz, arguments.getRotation());
        
        Vector3 location = arguments.getOrigin().toVector3Mutable().add(
                Vector3.of(FastMath.roundToInt(xz.getX()), y.apply(implementationArguments, scope).doubleValue(),
                           FastMath.roundToInt(xz.getZ()))).immutable();
        
        return apply(location, arguments.getWorld());
    }
    
    @Override
    public Position getPosition() {
        return position;
    }
    
    @Override
    public ReturnType returnType() {
        return ReturnType.STRING;
    }
    
    private String apply(Vector3 vector, WritableWorld world) {
        int y = vector.getBlockY();
        if(y >= world.getMaxHeight() || y < 0) return "AIR";
        SamplerProvider cache = ((NoiseChunkGenerator3D) world.getGenerator()).samplerProvider();
        double comp = sample(vector.getX(), vector.getY(), vector.getZ(), cache, world);
        
        if(comp > 0) return "LAND"; // If noise val is greater than zero, location will always be land.
        
        //BiomeProvider provider = tw.getBiomeProvider();
        //TerraBiome b = provider.getBiome(vector.getBlockX(), vector.getBlockZ());
        
        /*if(vector.getY() > c.getSeaLevel())*/
        return "AIR"; // Above sea level
        //return "OCEAN"; // Below sea level
    }
    
    private double sample(double x, double y, double z, SamplerProvider cache, World world) {
        int cx = FastMath.floorDiv((int) x, 16);
        int cz = FastMath.floorDiv((int) z, 16);
        return cache.getChunk(cx, cz, world, world.getBiomeProvider()).sample(x - (cx << 4), y, z - (cz << 4));
    }
}
