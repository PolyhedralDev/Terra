package com.dfsek.terra.generation;

import com.dfsek.terra.biome.grid.TerraBiomeGrid;
import org.bukkit.World;
import org.polydev.gaea.generation.GenerationPhase;
import org.polydev.gaea.math.FastNoiseLite;
import org.polydev.gaea.math.Interpolator;

public class ElevationInterpolator {
    private final UserDefinedGenerator[][] gens = new UserDefinedGenerator[10][10];
    private final double[][] values = new double[18][18];
    private final FastNoiseLite noise;
    private final int xOrigin;
    private final int zOrigin;
    private final TerraBiomeGrid grid;

    public ElevationInterpolator(World w, int chunkX, int chunkZ, TerraBiomeGrid grid, FastNoiseLite noise) {
        this.xOrigin = chunkX << 4;
        this.zOrigin = chunkZ << 4;
        this.noise = noise;
        this.grid = grid;

        for(int x = -2; x < 8; x++) {
            for(int z = -2; z < 8; z++) {
                gens[x + 2][z + 2] = (UserDefinedGenerator) grid.getBiome(xOrigin + x * 4, zOrigin + z * 4, GenerationPhase.BASE).getGenerator();
            }
        }

        for(byte x = -1; x <= 16; x++) {
            for(byte z = -1; z <= 16; z++) {
                UserDefinedGenerator generator = getGenerator(x, z);
                if(compareGens((x / 4), (z / 4)) && generator.interpolateElevation()) {
                    Interpolator interpolator = new Interpolator(biomeAvg(x / 4, z / 4),
                            biomeAvg((x / 4) + 1, z / 4),
                            biomeAvg(x / 4, (z / 4) + 1),
                            biomeAvg((x / 4) + 1, (z / 4) + 1),
                            Interpolator.Type.LINEAR);
                    values[x + 1][z + 1] = interpolator.bilerp((double) (x % 4) / 4, (double) (z % 4) / 4);
                } else values[x + 1][z + 1] = elevate(generator, xOrigin + x, zOrigin + z);
            }
        }
    }

    private UserDefinedGenerator getGenerator(int x, int z) {
        return (UserDefinedGenerator) grid.getBiome(xOrigin + x, zOrigin + z, GenerationPhase.BASE).getGenerator();
    }

    private UserDefinedGenerator getStoredGen(int x, int z) {
        return gens[x + 2][z + 2];
    }

    private boolean compareGens(int x, int z) {
        UserDefinedGenerator comp = getStoredGen(x, z);

        for(int xi = x - 2; xi <= x + 2; xi++) {
            for(int zi = z - 2; zi <= z + 2; zi++) {
                if(!comp.equals(getStoredGen(xi, zi))) return true;
            }
        }
        return false;
    }

    private double biomeAvg(int x, int z) {
        return (elevate(getStoredGen(x + 1, z), x * 4 + 4 + xOrigin, z * 4 + zOrigin)
                + elevate(getStoredGen(x - 1, z), x * 4 - 4 + xOrigin, z * 4 + zOrigin)
                + elevate(getStoredGen(x, z + 1), x * 4 + xOrigin, z * 4 + 4 + zOrigin)
                + elevate(getStoredGen(x, z - 1), x * 4 + xOrigin, z * 4 - 4 + zOrigin)
                + elevate(getStoredGen(x, z), x * 4 + xOrigin, z * 4 + zOrigin)
                + elevate(getStoredGen(x - 1, z - 1), x * 4 + xOrigin, z * 4 + zOrigin)
                + elevate(getStoredGen(x - 1, z + 1), x * 4 + xOrigin, z * 4 + zOrigin)
                + elevate(getStoredGen(x + 1, z - 1), x * 4 + xOrigin, z * 4 + zOrigin)
                + elevate(getStoredGen(x + 1, z + 1), x * 4 + xOrigin, z * 4 + zOrigin)) / 9D;
    }

    private double elevate(UserDefinedGenerator g, int x, int z) {
        if(g.getElevationEquation() != null) return g.getElevationEquation().getNoise(x, z, noise);
        return 0;
    }

    public double getElevation(int x, int z) {
        return values[x + 1][z + 1];
    }
}
