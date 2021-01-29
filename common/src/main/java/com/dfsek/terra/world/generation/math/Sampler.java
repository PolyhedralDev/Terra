package com.dfsek.terra.world.generation.math;

import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.biome.provider.BiomeProvider;
import com.dfsek.terra.world.generation.math.interpolation.BiomeChunkInterpolator;
import com.dfsek.terra.world.generation.math.interpolation.ElevationInterpolator;
import net.jafama.FastMath;

public class Sampler {
    private final BiomeChunkInterpolator interpolator;
    private final ElevationInterpolator elevationInterpolator;

    public Sampler(int x, int z, BiomeProvider provider, World world, int elevationSmooth) {
        this.interpolator = new BiomeChunkInterpolator(world, x, z, provider, (generator, coord) -> generator.getBaseSampler().getNoise(coord));
        this.elevationInterpolator = new ElevationInterpolator(world, x, z, provider, elevationSmooth);
    }

    public double sample(double x, double y, double z) {
        return interpolator.getNoise(x, y, z) + elevationInterpolator.getElevation(FastMath.roundToInt(x), FastMath.roundToInt(z));
    }

    public static double noise2dExtrude(double y, double base) {
        return ((-FastMath.pow2((y / base))) + 1);
    }
}
