package com.dfsek.terra.api.math.interpolation;

import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.world.biome.BiomeGrid;
import com.dfsek.terra.api.world.biome.Generator;
import com.dfsek.terra.api.world.generation.GenerationPhase;
import net.jafama.FastMath;

/**
 * Class to abstract away the 16 Interpolators needed to generate a chunk.<br>
 * Contains method to get interpolated noise at a coordinate within the chunk.
 */
public class ChunkInterpolator3 {
    private final Interpolator3[][][] interpGrid = new Interpolator3[4][64][4];
    private final Generator[][] gens = new Generator[7][7];
    private final boolean[][] needsBiomeInterp = new boolean[5][5];
    private final double[][][] noiseStorage = new double[7][7][65];
    private final int smooth;

    /**
     * Instantiates a 3D ChunkInterpolator at a pair of chunk coordinates, with a BiomeGrid and FastNoiseLite instance.
     *
     * @param chunkX X coordinate of the chunk.
     * @param chunkZ Z coordinate of the chunk.
     * @param grid   BiomeGrid to use for noise fetching.
     */
    public ChunkInterpolator3(World w, int chunkX, int chunkZ, BiomeGrid grid, int smooth) {
        int xOrigin = chunkX << 4;
        int zOrigin = chunkZ << 4;
        this.smooth = smooth;


        for(int x = -1; x < 6; x++) {
            for(int z = -1; z < 6; z++) {
                gens[x + 1][z + 1] = grid.getBiome(xOrigin + (x * smooth), zOrigin + (z * smooth), GenerationPhase.BASE).getGenerator();
            }
        }
        for(int x = 0; x < 5; x++) {
            for(int z = 0; z < 5; z++) {
                needsBiomeInterp[x][z] = compareGens(x + 1, z + 1);
            }
        }

        for(byte x = -1; x < 6; x++) {
            for(byte z = -1; z < 6; z++) {
                for(int y = 0; y < 65; y++) {
                    noiseStorage[x + 1][z + 1][y] = gens[x + 1][z + 1].getNoise(w, (x * smooth) + xOrigin, y << 2, (z * smooth) + zOrigin);
                }
            }
        }

        for(byte x = 0; x < 4; x++) {
            for(byte z = 0; z < 4; z++) {
                for(int y = 0; y < 64; y++) {
                    interpGrid[x][y][z] = new Interpolator3(
                            biomeAvg(x, y, z),
                            biomeAvg(x + 1, y, z),
                            biomeAvg(x, y + 1, z),
                            biomeAvg(x + 1, y + 1, z),
                            biomeAvg(x, y, z + 1),
                            biomeAvg(x + 1, y, z + 1),
                            biomeAvg(x, y + 1, z + 1),
                            biomeAvg(x + 1, y + 1, z + 1));
                }
            }
        }
    }

    private static int reRange(int value, int high) {
        return FastMath.max(FastMath.min(value, high), 0);
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

    private double biomeAvg(int x, int y, int z) {
        if(needsBiomeInterp[x][z]) {
            double t = 0d;
            for(int xi = 0; xi <= 2; xi++) {
                for(int zi = 0; zi <= 2; zi++) {
                    t += noiseStorage[x + xi][z + zi][y];
                }
            }
            return t / 9d;
        } else {
            return noiseStorage[x + 1][z + 1][y];
        }
    }

    /**
     * Gets the noise at a pair of internal chunk coordinates.
     *
     * @param x The internal X coordinate (0-15).
     * @param z The internal Z coordinate (0-15).
     * @return double - The interpolated noise at the coordinates.
     */
    public double getNoise(double x, double y, double z) {
        return interpGrid[reRange(((int) x) / smooth, 3)][reRange(((int) y) / 4, 63)][reRange(((int) z) / smooth, 3)].trilerp((x % smooth) / smooth, (y % 4) / 4, (z % smooth) / smooth);
    }
}
