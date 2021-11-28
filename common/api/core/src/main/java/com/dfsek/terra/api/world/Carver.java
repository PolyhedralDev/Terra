/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.world;


import com.dfsek.terra.api.world.access.World;
import com.dfsek.terra.api.world.chunk.ChunkAccess;


public interface Carver {
    void carve(World world, int chunkX, int chunkZ, ChunkAccess chunk);
}
