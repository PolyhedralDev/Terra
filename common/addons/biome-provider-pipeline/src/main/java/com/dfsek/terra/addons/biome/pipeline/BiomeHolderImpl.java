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
    private final int offset;
    private BiomeDelegate[][] biomes;
    
    public BiomeHolderImpl(int width, Vector2.Mutable origin) {
        width += 4;
        this.width = width;
        biomes = new BiomeDelegate[width][width];
        this.origin = origin;
        this.offset = 2;
    }
    
    private BiomeHolderImpl(BiomeDelegate[][] biomes, Vector2.Mutable origin, int width, int offset) {
        this.biomes = biomes;
        this.origin = origin;
        this.width = width;
        this.offset = 2 * offset;
    }
    
    @Override
    public BiomeHolder expand(BiomeExpander expander, long seed) {
        BiomeDelegate[][] old = biomes;
        int newWidth = width * 2 - 1;
        
        biomes = new BiomeDelegate[newWidth][newWidth];
        
        for(int x = 0; x < width; x++) {
            for(int z = 0; z < width; z++) {
                biomes[x * 2][z * 2] = old[x][z];
                if(z != width - 1)
                    biomes[x * 2][z * 2 + 1] = expander.getBetween(x + origin.getX(), z + 1 + origin.getZ(), seed, old[x][z],
                                                                   old[x][z + 1]);
                if(x != width - 1)
                    biomes[x * 2 + 1][z * 2] = expander.getBetween(x + 1 + origin.getX(), z + origin.getZ(), seed, old[x][z],
                                                                   old[x + 1][z]);
                if(x != width - 1 && z != width - 1)
                    biomes[x * 2 + 1][z * 2 + 1] = expander.getBetween(x + 1 + origin.getX(), z + 1 + origin.getZ(), seed, old[x][z],
                                                                       old[x + 1][z + 1], old[x][z + 1], old[x + 1][z]);
            }
        }
        return new BiomeHolderImpl(biomes, origin.setX(origin.getX() * 2 - 1).setZ(origin.getZ() * 2 - 1), newWidth, offset);
    }
    
    @Override
    public void mutate(BiomeMutator mutator, long seed) {
        for(int x = 0; x < width; x++) {
            for(int z = 0; z < width; z++) {
                BiomeMutator.ViewPoint viewPoint = new BiomeMutator.ViewPoint(this, x, z);
                biomes[x][z] = mutator.mutate(viewPoint, x + origin.getX(), z + origin.getZ(), seed);
            }
        }
    }
    
    @Override
    public void fill(BiomeSource source, long seed) {
        for(int x = 0; x < width; x++) {
            for(int z = 0; z < width; z++) {
                biomes[x][z] = source.getBiome(origin.getX() + x, origin.getZ() + z, seed);
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
