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
import com.dfsek.terra.api.util.vector.Vector2;


public class BiomeHolderImpl implements BiomeHolder {
    private final Vector2.Mutable origin;
    private final int width;
    private final double totalWidth;
    private BiomeDelegate[][] biomes;
    
    public BiomeHolderImpl(int width, double totalWidth, Vector2.Mutable origin) {
        this.totalWidth = totalWidth;
        this.width = width;
        biomes = new BiomeDelegate[width][width];
        this.origin = origin;
    }
    
    private BiomeHolderImpl(BiomeDelegate[][] biomes, Vector2.Mutable origin, int width, double totalWidth) {
        this.biomes = biomes;
        this.origin = origin;
        this.width = width;
        this.totalWidth = totalWidth;
    }
    
    private double normalise(double in) {
        return totalWidth * (in / width);
    }
    
    @Override
    public BiomeHolder expand(BiomeExpander expander, long seed) {
        BiomeDelegate[][] old = biomes;
        int newWidth = width * 2 - 1;
        
        biomes = new BiomeDelegate[newWidth][newWidth];
        
        for(int xi = 0; xi < width; xi++) {
            for(int zi = 0; zi < width; zi++) {
                biomes[xi * 2][zi * 2] = old[xi][zi];
                if(zi != width - 1)
                    biomes[xi * 2][zi * 2 + 1] = expander.getBetween(normalise(xi + origin.getX()), normalise(zi + 0.5 + origin.getZ()),
                                                                     seed, old[xi][zi],
                                                                     old[xi][zi + 1]);
                if(xi != width - 1)
                    biomes[xi * 2 + 1][zi * 2] = expander.getBetween(normalise(xi + 0.5 + origin.getX()), normalise(zi + origin.getZ()),
                                                                     seed, old[xi][zi],
                                                                     old[xi + 1][zi]);
                if(xi != width - 1 && zi != width - 1)
                    biomes[xi * 2 + 1][zi * 2 + 1] = expander.getBetween(normalise(xi + 0.5 + origin.getX()),
                                                                         normalise(zi + 0.5 + origin.getZ()), seed,
                                                                         old[xi][zi],
                                                                         old[xi + 1][zi + 1], old[xi][zi + 1], old[xi + 1][zi]);
            }
        }
        return new BiomeHolderImpl(biomes, origin.setX(origin.getX() * 2 - 1).setZ(origin.getZ() * 2 - 1), newWidth, totalWidth);
    }
    
    @Override
    public void mutate(BiomeMutator mutator, long seed) {
        for(int xi = 0; xi < width; xi++) {
            for(int zi = 0; zi < width; zi++) {
                BiomeMutator.ViewPoint viewPoint = new BiomeMutator.ViewPoint(this, xi, zi);
                biomes[xi][zi] = mutator.mutate(viewPoint, normalise(xi + origin.getX()), normalise(zi + origin.getZ()), seed);
            }
        }
    }
    
    @Override
    public void fill(BiomeSource source, long seed) {
        for(int xi = 0; xi < width; xi++) {
            for(int zi = 0; zi < width; zi++) {
                biomes[xi][zi] = source.getBiome(normalise(xi + origin.getX()), normalise(zi + origin.getZ()), seed);
            }
        }
    }
    
    @Override
    public BiomeDelegate getBiome(int x, int z) {
        return getBiomeRaw(x, z);
    }
    
    @Override
    public BiomeDelegate getBiomeRaw(int x, int z) {
        if(x >= width || z >= width || x < 0 || z < 0) return null;
        return biomes[x][z];
    }
}
