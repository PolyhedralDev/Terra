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
    
    public ExtrusionColumn(WorldProperties worldProperties, BiomeExtrusionProvider provider, int x, int z) {
        this.min = worldProperties.getMinHeight();
        this.max = worldProperties.getMaxHeight();
        this.provider = provider;
        this.x = x;
        this.z = z;
        this.seed = worldProperties.getSeed();
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
        return provider.getBiome(x, y, z, seed);
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
