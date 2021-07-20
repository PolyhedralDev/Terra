package com.dfsek.terra.addons.biome.pipeline.api;

import com.dfsek.terra.api.world.biome.TerraBiome;

public interface BiomeMutator {
    TerraBiome mutate(ViewPoint viewPoint, double x, double z, long seed);

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
