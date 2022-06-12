package com.dfsek.terra.addons.biome.extrusion;

import com.dfsek.terra.api.util.Column;
import com.dfsek.terra.api.util.function.IntIntObjConsumer;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.api.world.info.WorldProperties;


public class ExtrusionColumn implements Column<Biome> {
    private final int min;
    private final int max;
    private final BiomeExtrusionProvider provider;
    private final int x, z;
    private final long seed;
    private final Column<Biome> delegate;
    
    public ExtrusionColumn(int min, int max, BiomeExtrusionProvider provider, int x, int z, long seed) {
        this.min = min;
        this.max = max;
        this.provider = provider;
        this.x = x;
        this.z = z;
        this.seed = seed;
        this.delegate = provider.getDelegate().getColumn(x, z, seed, min, max);
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
        return provider.getBiome(x, y, z, seed, delegate.get(y));
    }
    
    @Override
    public void forRanges(IntIntObjConsumer<Biome> consumer) {
        int min = getMinY();
        
        int y = min;
        
        Biome runningObj = get(y);
        
        int runningMin = min;
        
        int max = getMaxY() - 1;
        
        while(y < max) {
            y += provider.getResolution();
            Biome current = get(y);
            
            if(!current.equals(runningObj)) {
                consumer.accept(runningMin, y, runningObj);
                runningMin = y;
                runningObj = current;
            }
        }
        consumer.accept(runningMin, ++y, runningObj);
    }
}
