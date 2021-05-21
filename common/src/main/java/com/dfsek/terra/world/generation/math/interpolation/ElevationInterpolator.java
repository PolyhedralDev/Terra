package com.dfsek.terra.world.generation.math.interpolation;

import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.world.biome.Generator;
import com.dfsek.terra.api.world.biome.provider.BiomeProvider;
import com.dfsek.terra.world.generation.WorldGenerator;

public class ElevationInterpolator {
    private final double[][] values = new double[18][18];

    public ElevationInterpolator(World world, int chunkX, int chunkZ, BiomeProvider provider, int smooth) {
        int xOrigin = chunkX << 4;
        int zOrigin = chunkZ << 4;

        WorldGenerator[][] gens = new WorldGenerator[18 + 2 * smooth][18 + 2 * smooth];

        // Precompute generators.
        for(int x = -1 - smooth; x <= 16 + smooth; x++) {
            for(int z = -1 - smooth; z <= 16 + smooth; z++) {
                gens[x + 1 + smooth][z + 1 + smooth] = (WorldGenerator) provider.getBiome(xOrigin + x, zOrigin + z).getGenerator(world);
            }
        }

        for(int x = -1; x <= 16; x++) {
            for(int z = -1; z <= 16; z++) {
                double noise = 0;
                double div = 0;
                for(int xi = -smooth; xi <= smooth; xi++) {
                    for(int zi = -smooth; zi <= smooth; zi++) {
                        Generator gen = gens[x + 1 + smooth + xi][z + 1 + smooth + zi];
                        noise += gen.getElevationSampler().getNoise(xOrigin + x, zOrigin + z) * gen.getElevationWeight();
                        div += gen.getElevationWeight();
                    }
                }
                values[x + 1][z + 1] = noise / div;
            }
        }
    }

    public double getElevation(int x, int z) {
        return values[x + 1][z + 1];
    }
}
