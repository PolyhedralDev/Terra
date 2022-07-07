package com.dfsek.terra.addons.biome.pipeline;

import java.util.function.Consumer;

import com.dfsek.terra.api.util.Column;
import com.dfsek.terra.api.util.function.IntIntObjConsumer;
import com.dfsek.terra.api.util.function.IntObjConsumer;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;


class BiomePipelineColumn implements Column<Biome> {
    private final int min;
    private final int max;
    
    private final int x;
    private final int z;
    private final Biome biome;
    
    protected BiomePipelineColumn(BiomeProvider biomeProvider, int min, int max, int x, int z, long seed) {
        this.min = min;
        this.max = max;
        this.x = x;
        this.z = z;
        this.biome = biomeProvider.getBiome(x, 0, z, seed);
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
        return biome;
    }
    
    @Override
    public void forRanges(int resolution, IntIntObjConsumer<Biome> consumer) {
        consumer.accept(min, max, biome);
    }
    
    @Override
    public void forEach(Consumer<Biome> consumer) {
        for(int y = min; y < max; y++) {
            consumer.accept(biome);
        }
    }
    
    @Override
    public void forEach(IntObjConsumer<Biome> consumer) {
        for(int y = min; y < max; y++) {
            consumer.accept(y, biome);
        }
    }
}
