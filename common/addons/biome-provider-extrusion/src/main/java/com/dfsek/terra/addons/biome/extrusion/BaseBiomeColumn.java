package com.dfsek.terra.addons.biome.extrusion;

import com.dfsek.terra.api.util.Column;
import com.dfsek.terra.api.world.biome.Biome;


class BaseBiomeColumn implements Column<Biome> {
    private final BiomeExtrusionProvider biomeProvider;
    private final Biome base;
    private final int min;
    private final int max;
    
    private final int x;
    private final int z;
    private final long seed;
    
    protected BaseBiomeColumn(BiomeExtrusionProvider biomeProvider, Biome base, int min, int max, int x, int z, long seed) {
        this.biomeProvider = biomeProvider;
        this.base = base;
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
        return biomeProvider.extrude(base, x, y, z, seed);
    }
}
