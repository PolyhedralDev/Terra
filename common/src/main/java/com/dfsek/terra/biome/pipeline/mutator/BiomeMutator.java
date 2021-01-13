package com.dfsek.terra.biome.pipeline.mutator;

import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.biome.pipeline.Position;

public interface BiomeMutator {
    Biome mutate(ViewPoint viewPoint, Position position);

    class ViewPoint {
        private final Biome[][] biomes;

        public ViewPoint(Biome[][] biomes) {
            this.biomes = biomes;
        }

        public Biome getBiome(int x, int z) {
            return biomes[x + 1][z + 1];
        }
    }
}
