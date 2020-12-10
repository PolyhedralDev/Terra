package com.dfsek.terra.api.gaea.math;

import com.dfsek.terra.api.gaea.biome.BiomeGrid;
import com.dfsek.terra.api.gaea.biome.Generator;
import com.dfsek.terra.api.gaea.generation.GenerationPhase;
import org.bukkit.World;

/**
 * Class to abstract away the 16 Interpolators needed to generate a chunk.<br>
 * Contains method to get interpolated noise at a coordinate within the chunk.
 */
public class ChunkInterpolator2 implements ChunkInterpolator {
    private final Interpolator[][] interpGrid = new Interpolator[4][4];

    private final int xOrigin;
    private final int zOrigin;
    private final FastNoiseLite noise;
    private final World w;

    /**
     * Instantiates a ChunkInterpolator at a pair of chunk coordinates, with a BiomeGrid and FastNoiseLite instance.
     *
     * @param chunkX X coordinate of the chunk.
     * @param chunkZ Z coordinate of the chunk.
     * @param grid   BiomeGrid to use for noise fetching.
     * @param noise  FastNoiseLite instance to use.
     */
    public ChunkInterpolator2(World w, int chunkX, int chunkZ, BiomeGrid grid, FastNoiseLite noise) {
        this.xOrigin = chunkX << 4;
        this.zOrigin = chunkZ << 4;
        this.noise = noise;
        this.w = w;
        Generator[][] gridTemp = new Generator[8][8];
        for(int x = - 2; x < 6; x++) {
            for(int z = - 2; z < 6; z++) {
                gridTemp[x + 2][z + 2] = grid.getBiome(xOrigin + (x << 2), zOrigin + (z << 2), GenerationPhase.BASE).getGenerator();
            }
        }
        for(byte x = 0; x < 4; x++) {
            for(byte z = 0; z < 4; z++) {
                interpGrid[x][z] = new Interpolator(biomeAvg(x, z, gridTemp),
                        biomeAvg(x + 1, z, gridTemp),
                        biomeAvg(x, z + 1, gridTemp),
                        biomeAvg(x + 1, z + 1, gridTemp), gridTemp[x+1][z+1].getInterpolationType());
            }
        }
    }

    private double biomeAvg(int x, int z, Generator[][] g) {
        return (g[x + 3][z + 2].getNoise(noise, w, (x << 2) + xOrigin, (z << 2) + zOrigin)
                + g[x + 1][z + 2].getNoise(noise, w, (x << 2) + xOrigin, (z << 2) + zOrigin)
                + g[x + 2][z + 3].getNoise(noise, w, (x << 2) + xOrigin, (z << 2) + zOrigin)
                + g[x + 2][z + 1].getNoise(noise, w, (x << 2) + xOrigin, (z << 2) + zOrigin)) / 4D;
    }

    /**
     * Gets the noise at a pair of internal chunk coordinates.
     *
     * @param x The internal X coordinate (0-15).
     * @param z The internal Z coordinate (0-15).
     * @return double - The interpolated noise at the coordinates.
     */
    @Override
    public double getNoise(double x, double z) {
        return interpGrid[((int) x) / 4][((int) z) / 4].bilerp((x % 4) / 4, (z % 4) / 4);
    }

    @Override
    public double getNoise(double x, double y, double z) {
        return getNoise(x, z);
    }
}
