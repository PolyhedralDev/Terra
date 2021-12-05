/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.biome.pipeline.api;

import com.dfsek.terra.api.world.biome.Biome;


public interface BiomeMutator {
    Biome mutate(ViewPoint viewPoint, double x, double z, long seed);
    
    default Iterable<Biome> getBiomes(Iterable<Biome> biomes) {
        return biomes;
    }
    
    class ViewPoint {
        private final BiomeHolder biomes;
        private final int offX;
        private final int offZ;
        
        public ViewPoint(BiomeHolder biomes, int offX, int offZ) {
            this.biomes = biomes;
            this.offX = offX;
            this.offZ = offZ;
        }
        
        
        public Biome getBiome(int x, int z) {
            return biomes.getBiomeRaw(x + offX, z + offZ);
        }
    }
}
