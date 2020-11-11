package com.dfsek.terra.generation;

import com.dfsek.terra.biome.TerraBiomeGrid;
import org.bukkit.World;
import org.polydev.gaea.biome.Generator;
import org.polydev.gaea.generation.GenerationPhase;
import org.polydev.gaea.math.FastNoiseLite;
import org.polydev.gaea.math.Interpolator;

public class ElevationInterpolator {
    private final UserDefinedGenerator[][] gens = new UserDefinedGenerator[7][7];
    private final double[][] values = new double[16][16];
    private final FastNoiseLite noise;
    private final int xOrigin;
    private final int zOrigin;

    public ElevationInterpolator(World w, int chunkX, int chunkZ, TerraBiomeGrid grid, FastNoiseLite noise) {
        this.xOrigin = chunkX << 4;
        this.zOrigin = chunkZ << 4;
        this.noise = noise;

        for(int x = -1; x < 6; x++) {
            for(int z = -1; z < 6; z++) {
                gens[x + 1][z + 1] = (UserDefinedGenerator) grid.getBiome(xOrigin + x * 4, zOrigin + z * 4, GenerationPhase.BASE).getGenerator();
            }
        }

        for(byte x = 0; x < 16; x++) {
            for(byte z = 0; z < 16; z++) {
                if(compareGens((x / 4) + 1, (z / 4) + 1)) {
                    Interpolator interpolator = new Interpolator(biomeAvg(x / 4, z / 4),
                            biomeAvg((x / 4) + 1, z / 4),
                            biomeAvg(x / 4, (z / 4) + 1),
                            biomeAvg((x / 4) + 1, (z / 4) + 1),
                            Interpolator.Type.LINEAR);
                    values[x][z] = interpolator.bilerp((double) (x % 4) / 4, (double) (z % 4) / 4);
                } else values[x][z] = elevate(gens[x / 4][z / 4], xOrigin + x, zOrigin + z);
            }
        }
    }

    private boolean compareGens(int x, int z) {
        Generator comp = gens[x][z];
        if(!comp.equals(gens[x + 1][z])) return true;

        if(!comp.equals(gens[x][z + 1])) return true;

        if(!comp.equals(gens[x - 1][z])) return true;

        if(!comp.equals(gens[x][z - 1])) return true;

        if(!comp.equals(gens[x + 1][z + 1])) return true;

        if(!comp.equals(gens[x - 1][z - 1])) return true;

        if(!comp.equals(gens[x + 1][z - 1])) return true;

        return !comp.equals(gens[x - 1][z + 1]);
    }

    private double biomeAvg(int x, int z) {
        return (elevate(gens[x + 2][z + 1], x * 4 + 4 + xOrigin, z * 4 + zOrigin)
                + elevate(gens[x][z + 1], x * 4 - 4 + xOrigin, z * 4 + zOrigin)
                + elevate(gens[x + 1][z + 2], x * 4 + xOrigin, z * 4 + 4 + zOrigin)
                + elevate(gens[x + 1][z], x * 4 + xOrigin, z * 4 - 4 + zOrigin)
                + elevate(gens[x + 1][z + 1], x * 4 + xOrigin, z * 4 + zOrigin)) / 5D;
    }

    private double elevate(UserDefinedGenerator g, int x, int z) {
        if(g.getElevationEquation() != null) return g.getElevationEquation().getNoise(x, z, noise);
        return 0;
    }

    public double getElevation(int x, int z) {
        return values[x][z];
    }
}
