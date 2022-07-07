package com.dfsek.terra.api.world.biome.generation;

import com.dfsek.terra.api.util.Column;
import com.dfsek.terra.api.world.biome.Biome;


class BiomeColumn implements Column<Biome> {
    private final BiomeProvider biomeProvider;
    private final int min;
    private final int max;
    
    private final int x;
    private final int z;
    private final long seed;
    
    protected BiomeColumn(BiomeProvider biomeProvider, int min, int max, int x, int z, long seed) {
        this.biomeProvider = biomeProvider;
        this.min = min;
        this.max = max;
        this.x = x;
        this.z = z;
        this.seed = seed;
    }
    
    @Override
    public int getMinY() {
        return min;
    }
    
    @Override
    public int getMaxY() {
        return max;
    }
    
    @Override
    public int getX() {
        return x;
    }
    
    @Override
    public int getZ() {
        return z;
    }
    
    @Override
    public Biome get(int y) {
        return biomeProvider.getBiome(x, y, z, seed);
    }
}
