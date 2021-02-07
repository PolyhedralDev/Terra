package com.dfsek.terra.biome.pipeline.mutator;

import com.dfsek.terra.biome.TerraBiome;
import com.dfsek.terra.biome.pipeline.BiomeHolder;

public interface BiomeMutator {
    TerraBiome mutate(ViewPoint viewPoint, double x, double z);

    class ViewPoint {
        private final BiomeHolder biomes;
        private final int offX;
        private final int offZ;

        public ViewPoint(BiomeHolder biomes, int offX, int offZ) {
            this.biomes = biomes;
            this.offX = offX;
            this.offZ = offZ;
        }


        public TerraBiome getBiome(int x, int z) {
            return biomes.getBiomeRaw(x + offX, z + offZ);
        }
    }
}
