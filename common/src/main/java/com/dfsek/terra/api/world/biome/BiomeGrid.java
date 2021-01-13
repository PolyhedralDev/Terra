package com.dfsek.terra.api.world.biome;

import com.dfsek.terra.api.math.noise.samplers.FastNoiseLite;
import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.world.generation.GenerationPhase;

public abstract class BiomeGrid {
    private final FastNoiseLite noiseX;
    private final FastNoiseLite noiseZ;
    private final int sizeX;
    private final int sizeZ;
    private TerraBiome[][] grid;


    public BiomeGrid(long seed, double freq1, double freq2, int sizeX, int sizeZ) {
        this.sizeX = sizeX;
        this.sizeZ = sizeZ;
        this.noiseX = new FastNoiseLite((int) seed);
        this.noiseZ = new FastNoiseLite((int) seed + 1);
        this.noiseX.setNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
        this.noiseX.setFractalType(FastNoiseLite.FractalType.FBm);
        this.noiseX.setFractalOctaves(4);
        this.noiseZ.setNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
        this.noiseZ.setFractalType(FastNoiseLite.FractalType.FBm);
        this.noiseZ.setFractalOctaves(4);
        this.noiseX.setFrequency(freq1);
        this.noiseZ.setFrequency(freq2);
    }


    /**
     * Gets the biome at a pair of coordinates.
     *
     * @param x - X-coordinate at which to fetch biome
     * @param z - Z-coordinate at which to fetch biome
     * @return TerraBiome - TerraBiome at the given coordinates.
     */
    public TerraBiome getBiome(int x, int z, GenerationPhase phase) {
        return grid[getBiomeNoiseX(x, z)][getBiomeNoiseZ(x, z)];
    }

    /**
     * Gets the biome at a location.
     *
     * @param l - The location at which to fetch the biome.
     * @return TerraBiome - TerraBiome at the given coordinates.
     */
    public TerraBiome getBiome(Location l) {
        return getBiome(l, GenerationPhase.POST_GEN);
    }

    public double[] getRawNoise(int x, int z) {
        return new double[] {noiseX.getNoise(x, z), noiseZ.getNoise(x, z)};
    }

    /**
     * Get the raw X-noise for coordinates in the Grid.
     *
     * @param x X coordinate
     * @param z Z coordinate
     * @return Normalized noise
     */
    public int getBiomeNoiseX(int x, int z) {
        return normalize(noiseX.getNoise(x, z), sizeX);
    }

    /**
     * Get the raw Z-noise for coordinates in the Grid.
     *
     * @param x X coordinate
     * @param z Z coordinate
     * @return Normalized noise
     */
    public int getBiomeNoiseZ(int x, int z) {
        return normalize(noiseZ.getNoise(x, z), sizeZ);
    }

    public TerraBiome[][] getGrid() {
        return grid;
    }

    public void setGrid(TerraBiome[][] grid) {
        if(grid.length != sizeX) throw new IllegalArgumentException("Invalid length for grid, expected " + sizeX + ", got " + grid.length);
        for(TerraBiome[] gridLayer : grid) {
            if(gridLayer.length != sizeZ)
                throw new IllegalArgumentException("Invalid length for grid layer, expected " + sizeZ + ", got " + gridLayer.length);
        }
        this.grid = grid;
    }

    public TerraBiome getBiome(Location l, GenerationPhase phase) {
        double biomeNoise = noiseX.getNoise(l.getBlockX(), l.getBlockZ());
        double climateNoise = noiseZ.getNoise(l.getBlockX(), l.getBlockZ());
        return grid[normalize(biomeNoise, sizeX)][normalize(climateNoise, sizeZ)];
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeZ() {
        return sizeZ;
    }

    /**
     * Takes a noise input and normalizes it to a value between 0 and 15 inclusive.
     *
     * @param i - The noise value to normalize.
     * @return int - The normalized value.
     */
    protected int normalize(double i, int range) {
        return NormalizationUtil.normalize(i, range, 4);
    }

}
