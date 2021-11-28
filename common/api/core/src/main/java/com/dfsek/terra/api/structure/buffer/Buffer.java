/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.structure.buffer;

import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.api.world.chunk.Chunk;
import com.dfsek.terra.api.world.access.ServerWorld;

import org.jetbrains.annotations.ApiStatus.Experimental;


@Experimental
public interface Buffer {
    void paste(Vector3 origin, Chunk chunk);
    
    void paste(Vector3 origin, ServerWorld world);
    
    Buffer addItem(BufferedItem item, Vector3 location);
    
    Buffer setMark(String mark, Vector3 location);
    
    Vector3 getOrigin();
    
    String getMark(Vector3 location);
}
