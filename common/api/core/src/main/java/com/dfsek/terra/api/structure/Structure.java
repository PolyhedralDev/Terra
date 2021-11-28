/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.structure;

import java.util.Random;

import com.dfsek.terra.api.structure.buffer.Buffer;
import com.dfsek.terra.api.util.Rotation;
import com.dfsek.terra.api.util.StringIdentifiable;
import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.api.world.access.WritableWorld;
import com.dfsek.terra.api.world.chunk.Chunk;


public interface Structure extends StringIdentifiable {
    boolean generate(Vector3 location, WritableWorld world, Chunk chunk, Random random, Rotation rotation);
    
    boolean generate(Buffer buffer, WritableWorld world, Random random, Rotation rotation, int recursions);
    
    boolean generate(Vector3 location, WritableWorld world, Random random, Rotation rotation);
}
