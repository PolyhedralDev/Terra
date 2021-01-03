package com.dfsek.terra.generation.math;

import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.biome.grid.master.TerraBiomeGrid;
import com.dfsek.terra.generation.math.interpolation.ChunkInterpolator;
import net.jafama.FastMath;

public class Sampler {
    private final ChunkInterpolator interpolator;
    private final ElevationInterpolator elevationInterpolator;

    public Sampler(int x, int z, TerraBiomeGrid grid, World world, int elevationSmooth, int generationSmooth) {
        this.interpolator = new ChunkInterpolator(world, x, z, grid, generationSmooth);
        this.elevationInterpolator = new ElevationInterpolator(world, x, z, grid, elevationSmooth);
    }

    public double sample(double x, double y, double z) {
        return interpolator.getNoise(x, y, z) + elevationInterpolator.getElevation(FastMath.roundToInt(x), FastMath.roundToInt(z));
    }
}
