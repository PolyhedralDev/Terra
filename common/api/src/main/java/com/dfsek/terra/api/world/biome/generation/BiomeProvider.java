/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.world.biome.generation;

import com.dfsek.terra.api.util.vector.Vector2;
import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.api.world.biome.Biome;


public interface BiomeProvider {
    Biome getBiome(int x, int z, long seed);
    
    default Biome getBiome(Vector2 vector2, long seed) {
        return getBiome(vector2.getBlockX(), vector2.getBlockZ(), seed);
    }
    
    default Biome getBiome(Vector3 vector3, long seed) {
        return getBiome(vector3.getBlockX(), vector3.getBlockZ(), seed);
    }
    
    Iterable<Biome> getBiomes();
}
