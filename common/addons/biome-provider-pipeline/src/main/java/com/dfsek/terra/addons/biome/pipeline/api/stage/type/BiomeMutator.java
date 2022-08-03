/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.biome.pipeline.api.stage.type;

import com.dfsek.terra.addons.biome.pipeline.api.BiomeHolder;
import com.dfsek.terra.addons.biome.pipeline.api.delegate.BiomeDelegate;


public interface BiomeMutator {
    BiomeDelegate mutate(ViewPoint viewPoint, double x, double z, long seed);
    
    default Iterable<BiomeDelegate> getBiomes(Iterable<BiomeDelegate> biomes) {
        return biomes;
    }
    
    class ViewPoint {
        private final BiomeHolder biomes;
        private final int ratio;
        private int offX;
        private int offZ;
        
        public ViewPoint(BiomeHolder biomes, int offX, int offZ, int ratio) {
            this.biomes = biomes;
            this.offX = offX;
            this.offZ = offZ;
            this.ratio = ratio;
        }
        
        public void setX(int i) {
            this.offX = i;
        }
        
        public void setZ(int i) {
            this.offZ += i;
        }
        
        public BiomeDelegate getBiome(int x, int z) {
            return biomes.getBiomeRaw(x * ratio + offX, z * ratio + offZ);
        }
    }
}
