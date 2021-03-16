package com.dfsek.terra.world.generation.math.samplers;

import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.world.biome.provider.BiomeProvider;
import com.dfsek.terra.world.generation.math.interpolation.ChunkInterpolator3D;
import com.dfsek.terra.world.generation.math.interpolation.ElevationInterpolator;
import net.jafama.FastMath;

public class Sampler3D implements Sampler {
    private final ChunkInterpolator3D interpolator;
    private final ElevationInterpolator elevationInterpolator;

    public Sampler3D(int x, int z, BiomeProvider provider, World world, int elevationSmooth) {
        this.interpolator = new ChunkInterpolator3D(world, x, z, provider, (generator, coord) -> generator.getBaseSampler().getNoise(coord));
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
