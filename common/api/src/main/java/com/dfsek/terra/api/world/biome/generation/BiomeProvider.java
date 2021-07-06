package com.dfsek.terra.api.world.biome.generation;

import com.dfsek.terra.api.vector.Vector2;
import com.dfsek.terra.api.vector.Vector3;
import com.dfsek.terra.api.world.biome.TerraBiome;

public interface BiomeProvider {
    TerraBiome getBiome(int x, int z);

    default TerraBiome getBiome(Vector2 vector2) {
        return getBiome(vector2.getBlockX(), vector2.getBlockZ());
    }

    default TerraBiome getBiome(Vector3 vector3) {
        return getBiome(vector3.getBlockX(), vector3.getBlockZ());
    }

    @Deprecated
    enum Type {
        IMAGE, PIPELINE, SINGLE
    }
}
