/*
 * Copyright (c) 2020-2025 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.world;

import com.dfsek.seismic.type.vector.Vector3;
import com.dfsek.terra.api.world.chunk.Chunk;


public interface ServerWorld extends WritableWorld {
    Chunk getChunkAt(int x, int z);

    default Chunk getChunkAt(Vector3 location) {
        return getChunkAt(location.getFloorX() >> 4, location.getFloorZ() >> 4);
    }
}
