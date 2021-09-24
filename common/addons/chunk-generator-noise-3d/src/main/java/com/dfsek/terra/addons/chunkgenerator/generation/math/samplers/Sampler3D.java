package com.dfsek.terra.addons.chunkgenerator.generation.math.samplers;

import net.jafama.FastMath;

import com.dfsek.terra.addons.chunkgenerator.generation.math.interpolation.ChunkInterpolator3D;
import com.dfsek.terra.addons.chunkgenerator.generation.math.interpolation.ElevationInterpolator;
import com.dfsek.terra.api.world.World;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.util.math.Sampler;


public class Sampler3D implements Sampler {
    private final ChunkInterpolator3D interpolator;
    private final ElevationInterpolator elevationInterpolator;
    
    public Sampler3D(int x, int z, BiomeProvider provider, World world, int elevationSmooth) {
        this.interpolator = new ChunkInterpolator3D(world, x, z, provider, (generator, coord) -> generator.getBaseSampler()
                                                                                                          .getNoiseSeeded(coord,
                                                                                                                          world.getSeed()));
        this.elevationInterpolator = new ElevationInterpolator(world, x, z, provider, elevationSmooth);
    }
    
    @Override
    public double sample(double x, double y, double z) {
        return interpolator.getNoise(x, y, z) + elevationInterpolator.getElevation(FastMath.roundToInt(x), FastMath.roundToInt(z));
    }
    
    @Override
    public double sample(int x, int y, int z) {
        return interpolator.getNoise(x, y, z) + elevationInterpolator.getElevation(FastMath.roundToInt(x), FastMath.roundToInt(z));
    }
}
