/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.biome.pipeline;

import com.dfsek.terra.addons.biome.pipeline.api.BiomeHolder;
import com.dfsek.terra.addons.biome.pipeline.api.delegate.BiomeDelegate;
import com.dfsek.terra.addons.biome.pipeline.api.stage.type.BiomeExpander;
import com.dfsek.terra.addons.biome.pipeline.api.stage.type.BiomeMutator;
import com.dfsek.terra.addons.biome.pipeline.source.BiomeSource;


public class BiomeHolderImpl implements BiomeHolder {
    private final int width;
    private final int offset;
    private final double totalWidth;
    private final int originalWidth;
    private BiomeDelegate[][] biomes;
    private final int resolution;
    
    public BiomeHolderImpl(int width, double totalWidth, int resolution) {
        this.totalWidth = totalWidth;
        this.originalWidth = width;
        this.resolution = resolution;
        width += 4;
        this.width = width;
        biomes = new BiomeDelegate[width][width];
        this.offset = 2;
    }
    
    private BiomeHolderImpl(BiomeDelegate[][] biomes, int width, int offset, double totalWidth, int originalWidth, int resolution) {
        this.biomes = biomes;
        this.width = width;
        this.offset = 2 * offset;
        this.totalWidth = totalWidth;
        this.originalWidth = originalWidth;
        this.resolution = resolution;
    }
    
    private double normalise(double in) {
        return totalWidth * ((in - offset) / originalWidth);
    }
    
    @Override
    public BiomeHolder expand(BiomeExpander expander, int x, int z, long seed) {
        BiomeDelegate[][] old = biomes;
        int newWidth = width * 2 - 1;
        
        biomes = new BiomeDelegate[newWidth][newWidth];
        
        
        for(int xi = 0; xi < width; xi++) {
            for(int zi = 0; zi < width; zi++) {
                biomes[xi * 2][zi * 2] = old[xi][zi];
                if(zi != width - 1)
                    biomes[xi * 2][zi * 2 + 1] = expander.getBetween((normalise(xi) + x) * resolution, (normalise(zi + 1) + z) * resolution, seed, old[xi][zi],
                                                                     old[xi][zi + 1]);
                if(xi != width - 1)
                    biomes[xi * 2 + 1][zi * 2] = expander.getBetween((normalise(xi + 1) + x) * resolution, (normalise(zi) + z) * resolution, seed, old[xi][zi],
                                                                     old[xi + 1][zi]);
                if(xi != width - 1 && zi != width - 1)
                    biomes[xi * 2 + 1][zi * 2 + 1] = expander.getBetween((normalise(xi + 1) + x) * resolution, (normalise(zi + 1) + z) * resolution, seed,
                                                                         old[xi][zi],
                                                                         old[xi + 1][zi + 1], old[xi][zi + 1], old[xi + 1][zi]);
            }
        }
        return new BiomeHolderImpl(biomes, newWidth, offset, totalWidth, originalWidth * 2 - 1, resolution);
    }
    
    @Override
    public void mutate(BiomeMutator mutator, int x, int z, long seed) {
        for(int xi = 0; xi < width; xi++) {
            for(int zi = 0; zi < width; zi++) {
                BiomeMutator.ViewPoint viewPoint = new BiomeMutator.ViewPoint(this, xi, zi);
                biomes[xi][zi] = mutator.mutate(viewPoint, (normalise(xi) + x) * resolution, (normalise(zi) + z) * resolution, seed);
            }
        }
    }
    
    @Override
    public void fill(BiomeSource source, int x, int z, long seed) {
        for(int xi = 0; xi < width; xi++) {
            for(int zi = 0; zi < width; zi++) {
                biomes[xi][zi] = source.getBiome((normalise(xi) + x) * resolution, (normalise(zi) + z) * resolution, seed);
            }
        }
    }
    
    @Override
    public BiomeDelegate getBiome(int x, int z) {
        x += offset;
        z += offset;
        return getBiomeRaw(x, z);
    }
    
    @Override
    public BiomeDelegate getBiomeRaw(int x, int z) {
        if(x >= width || z >= width || x < 0 || z < 0) return null;
        return biomes[x][z];
    }
}
