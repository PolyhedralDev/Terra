/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addon.terrascript.check;

import com.dfsek.terra.addons.terrascript.Type;

import net.jafama.FastMath;

import com.dfsek.terra.addons.chunkgenerator.generation.NoiseChunkGenerator3D;
import com.dfsek.terra.addons.chunkgenerator.generation.math.samplers.SamplerProvider;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.Expression;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.Scope;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.functions.Function;
import com.dfsek.terra.addons.terrascript.legacy.script.TerraImplementationArguments;
import com.dfsek.terra.addons.terrascript.lexer.SourcePosition;
import com.dfsek.terra.api.util.RotationUtil;
import com.dfsek.terra.api.util.vector.Vector2;
import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.api.world.World;
import com.dfsek.terra.api.world.WritableWorld;


public class CheckFunction implements Function<String> {
    private final Expression<Number> x, y, z;
    private final SourcePosition position;
    
    public CheckFunction(Expression<Number> x, Expression<Number> y, Expression<Number> z, SourcePosition position) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.position = position;
    }
    
    
    @Override
    public String evaluate(ImplementationArguments implementationArguments, Scope scope) {
        
        
        TerraImplementationArguments arguments = (TerraImplementationArguments) implementationArguments;
        
        
        Vector2 xz = Vector2.of(x.evaluate(implementationArguments, scope).doubleValue(),
                                z.evaluate(implementationArguments, scope).doubleValue());
        
        RotationUtil.rotateVector(xz, arguments.getRotation());
        
        Vector3 location = arguments.getOrigin().toVector3Mutable().add(
                Vector3.of(FastMath.roundToInt(xz.getX()), y.evaluate(implementationArguments, scope).doubleValue(),
                           FastMath.roundToInt(xz.getZ()))).immutable();
        
        return apply(location, arguments.getWorld());
    }
    
    @Override
    public SourcePosition getPosition() {
        return position;
    }
    
    @Override
    public Type returnType() {
        return Type.STRING;
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
