/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.world;

import java.util.List;

import com.dfsek.terra.api.util.Range;
import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.api.world.chunk.Chunk;


public interface Flora {
    boolean plant(Vector3 l, World world);
    
    List<Vector3> getValidSpawnsAt(Chunk chunk, int x, int z, Range check);
}
