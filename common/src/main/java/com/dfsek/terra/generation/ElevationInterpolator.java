package com.dfsek.terra.generation;

import com.dfsek.terra.api.math.interpolation.Interpolator;
import com.dfsek.terra.api.world.generation.GenerationPhase;
import com.dfsek.terra.biome.grid.master.TerraBiomeGrid;
import com.dfsek.terra.generation.config.WorldGenerator;

public class ElevationInterpolator {
    private final WorldGenerator[][] gens;
    private final double[][] values = new double[18][18];
    private final int xOrigin;
    private final int zOrigin;
    private final TerraBiomeGrid grid;
    private final int smooth;

    public ElevationInterpolator(int chunkX, int chunkZ, TerraBiomeGrid grid, int smooth) {
        this.xOrigin = chunkX << 4;
        this.zOrigin = chunkZ << 4;
        this.grid = grid;
        this.smooth = smooth;
        this.gens = new WorldGenerator[10][10];

        for(int x = -2; x < 8; x++) {
            for(int z = -2; z < 8; z++) {
                gens[x + 2][z + 2] = (WorldGenerator) grid.getBiome(xOrigin + (x * smooth), zOrigin + (z * smooth), GenerationPhase.BASE).getGenerator();
            }
        }

        for(byte x = -1; x <= 16; x++) {
            for(byte z = -1; z <= 16; z++) {
                WorldGenerator generator = getGenerator(x, z);
                if(compareGens((x / smooth), (z / smooth), generator) && generator.interpolateElevation()) {
                    Interpolator interpolator = new Interpolator(biomeAvg(x / smooth, z / smooth),
                            biomeAvg((x / smooth) + 1, z / smooth),
                            biomeAvg(x / smooth, (z / smooth) + 1),
                            biomeAvg((x / smooth) + 1, (z / smooth) + 1));
                    values[x + 1][z + 1] = interpolator.bilerp((double) (x % smooth) / smooth, (double) (z % smooth) / smooth);
                } else values[x + 1][z + 1] = elevate(generator, xOrigin + x, zOrigin + z);
            }
        }
    }

    private WorldGenerator getGenerator(int x, int z) {
        return (WorldGenerator) grid.getBiome(xOrigin + x, zOrigin + z, GenerationPhase.BASE).getGenerator();
    }

    private WorldGenerator getStoredGen(int x, int z) {
        return gens[x + 2][z + 2];
    }

    private boolean compareGens(int x, int z, WorldGenerator comp) {
        for(int xi = x - 2; xi <= x + 2; xi++) {
            for(int zi = z - 2; zi <= z + 2; zi++) {
                if(!comp.equals(getStoredGen(xi, zi))) return true;
            }
        }
        return false;
    }

    private double biomeAvg(int x, int z) {
        return (elevate(getStoredGen(x + 1, z), (x * smooth) + smooth + xOrigin, (z * smooth) + zOrigin)
                + elevate(getStoredGen(x - 1, z), (x * smooth) - smooth + xOrigin, (z * smooth) + zOrigin)
                + elevate(getStoredGen(x, z + 1), (x * smooth) + xOrigin, (z * smooth) + smooth + zOrigin)
                + elevate(getStoredGen(x, z - 1), (x * smooth) + xOrigin, (z * smooth) - smooth + zOrigin)
                + elevate(getStoredGen(x, z), (x * smooth) + xOrigin, (z * smooth) + zOrigin)
                + elevate(getStoredGen(x - 1, z - 1), (x * smooth) - smooth + xOrigin, (z * smooth) - smooth + zOrigin)
                + elevate(getStoredGen(x - 1, z + 1), (x * smooth) - smooth + xOrigin, (z * smooth) + smooth + zOrigin)
                + elevate(getStoredGen(x + 1, z - 1), (x * smooth) + smooth + xOrigin, (z * smooth) - smooth + zOrigin)
                + elevate(getStoredGen(x + 1, z + 1), (x * smooth) + smooth + xOrigin, (z * smooth) + smooth + zOrigin)) / 9D;
    }

    private double elevate(WorldGenerator g, int x, int z) {
        return g.getElevation(x, z);
    }

    public double getElevation(int x, int z) {
        return values[x + 1][z + 1];
    }
}
