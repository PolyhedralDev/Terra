package com.dfsek.terra.biome;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.math.vector.Vector2;
import com.dfsek.terra.api.math.vector.Vector3;
import com.dfsek.terra.api.world.biome.TerraBiome;

public interface BiomeProvider {
    TerraBiome getBiome(int x, int z);

    default TerraBiome getBiome(Vector2 vector2) {
        return getBiome(vector2.getBlockX(), vector2.getBlockZ());
    }

    default TerraBiome getBiome(Vector3 vector3) {
        return getBiome(vector3.getBlockX(), vector3.getBlockZ());
    }

    default TerraBiome getBiome(Location location) {
        return getBiome(location.getBlockX(), location.getBlockZ());
    }

    interface BiomeProviderBuilder {
        BiomeProvider build(long seed);
    }
}
