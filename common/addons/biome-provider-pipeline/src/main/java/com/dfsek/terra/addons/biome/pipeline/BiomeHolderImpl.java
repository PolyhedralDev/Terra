package com.dfsek.terra.addons.biome.pipeline;

import com.dfsek.terra.addons.biome.pipeline.api.BiomeHolder;
import com.dfsek.terra.addons.biome.pipeline.api.delegate.BiomeDelegate;
import com.dfsek.terra.addons.biome.pipeline.api.stage.type.BiomeExpander;
import com.dfsek.terra.addons.biome.pipeline.api.stage.type.BiomeMutator;
import com.dfsek.terra.addons.biome.pipeline.source.BiomeSource;
import com.dfsek.terra.api.util.vector.Vector2Int;


public class BiomeHolderImpl implements BiomeHolder {
    private final BiomeDelegate[][] biomeArray;
    private int totalWidth;
    private int ratio;
    private int offset;
    private final Vector2Int.Mutable origin;
    private final Vector2Int.Mutable arrayOrigin;
    
    public BiomeHolderImpl(int totalWidth, int ratio, Vector2Int.Mutable origin) {
        biomeArray = new BiomeDelegate[totalWidth][totalWidth];
        this.origin = origin;
        this.totalWidth = totalWidth;
        this.ratio = ratio;
        arrayOrigin = Vector2Int.of(0, 0).mutable();
        int offset = 2;
    }
    
    @Override
    public void expand(BiomeExpander expander, long seed) {
        int newRatio = ratio / 2;
        for(int xi = arrayOrigin.getX(); xi < totalWidth - arrayOrigin.getX(); xi += ratio) {
            for(int zi = arrayOrigin.getZ(); zi < totalWidth - arrayOrigin.getX(); zi += ratio) {
                
                if(zi + ratio < totalWidth - arrayOrigin.getZ())
                    biomeArray[xi][zi + newRatio] = expander.getBetween(xi + origin.getX(), zi + newRatio + origin.getZ(),
                                                            seed, biomeArray[xi][zi],
                                                            biomeArray[xi][zi + ratio]);
                
                if(xi + ratio < totalWidth - arrayOrigin.getX())
                    biomeArray[xi + newRatio][zi] = expander.getBetween(xi + newRatio + origin.getX(), zi + origin.getZ(),
                                                                         seed, biomeArray[xi][zi],
                                                                         biomeArray[xi + ratio][zi]);
                if(xi + ratio < totalWidth - arrayOrigin.getX() && zi + ratio < totalWidth - arrayOrigin.getZ())
                    biomeArray[xi + newRatio][zi + newRatio] = expander.getBetween(xi + newRatio + origin.getX(),
                                                                         zi + newRatio + origin.getZ(), seed,
                                                                             biomeArray[xi][zi],
                                                                             biomeArray[xi + ratio][zi + ratio], biomeArray[xi][zi + ratio], biomeArray[xi + ratio][zi]);
            }
        }
        ratio = newRatio;
    }
    
    @Override
    public void mutate(BiomeMutator mutator, long seed) {
        arrayOrigin.setX(arrayOrigin.getX() + ratio);
        arrayOrigin.setZ(arrayOrigin.getZ() + ratio);
    
        
        
        for(int xi = arrayOrigin.getX(); xi < totalWidth - arrayOrigin.getX(); xi += ratio) {
            for(int zi = arrayOrigin.getZ(); zi < totalWidth - arrayOrigin.getZ(); zi += ratio) {
                BiomeMutator.ViewPoint viewPoint = new BiomeMutator.ViewPoint(this, xi, zi, ratio);
                
                biomeArray[xi][zi] = mutator.mutate(viewPoint, xi + origin.getX(), zi + origin.getZ(), seed);
            }
        }
        
    }
    
    @Override
    public void fill(BiomeSource source, long seed) {
        for(int xi = arrayOrigin.getX(); xi < totalWidth - arrayOrigin.getX(); xi += ratio) {
            for(int zi = arrayOrigin.getZ(); zi < totalWidth - arrayOrigin.getX(); zi += ratio) {
                biomeArray[xi][zi] = source.getBiome(xi + origin.getX(), zi + origin.getZ(), seed);
            }
        }
    }
    
    @Override
    public BiomeDelegate getBiome(int x, int z) {
        if(x < 0 || z < 0) return null;
        int xi = x + arrayOrigin.getX();
        int zi = z + arrayOrigin.getZ();
        return getBiomeRaw(xi, zi);
    }
    
    @Override
    public BiomeDelegate getBiomeRaw(int xi, int zi) {
        if(xi >= totalWidth || zi >= totalWidth || xi < 0 || zi < 0) return null;
        return biomeArray[xi][zi];
    }
}
