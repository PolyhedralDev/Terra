package com.dfsek.terra.biome.pipeline.mutator;

import com.dfsek.terra.api.world.biome.TerraBiome;
import com.dfsek.terra.biome.pipeline.Position;

public interface BiomeMutator {
    TerraBiome mutate(ViewPoint viewPoint, Position position);

    class ViewPoint {
        private final TerraBiome[][] biomes;

        public ViewPoint(TerraBiome[][] biomes) {
            this.biomes = biomes;
        }

        public TerraBiome getBiome(int x, int z) {
            return biomes[x + 1][z + 1];
        }
    }
}
