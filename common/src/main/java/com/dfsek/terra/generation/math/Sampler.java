package com.dfsek.terra.generation.math;

import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.biome.BiomeProvider;
import com.dfsek.terra.generation.math.interpolation.BiomeChunkInterpolator;
import com.dfsek.terra.generation.math.interpolation.ElevationInterpolator;
import net.jafama.FastMath;

public class Sampler {
    private final BiomeChunkInterpolator interpolator;
    private final ElevationInterpolator elevationInterpolator;

    public Sampler(int x, int z, BiomeProvider provider, World world, int elevationSmooth) {
        this.interpolator = new BiomeChunkInterpolator(world, x, z, provider);
        this.elevationInterpolator = new ElevationInterpolator(world, x, z, provider, elevationSmooth);
    }

    public double sample(double x, double y, double z) {
        return interpolator.getNoise(x, y, z) + elevationInterpolator.getElevation(FastMath.roundToInt(x), FastMath.roundToInt(z));
    }
}
