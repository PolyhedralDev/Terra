/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.structure.buffer.buffers;

import java.util.LinkedHashMap;
import java.util.Map;

import com.dfsek.terra.api.structure.buffer.Buffer;
import com.dfsek.terra.api.structure.buffer.BufferedItem;
import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.api.world.Chunk;
import com.dfsek.terra.api.world.World;


/**
 * Buffer implementation that directly pastes to the world.
 */
public class DirectBuffer implements Buffer {
    private final Vector3 origin;
    private final World target;
    private final Map<Vector3, String> marks = new LinkedHashMap<>();
    
    public DirectBuffer(Vector3 origin, World target) {
        this.origin = origin;
        this.target = target;
    }
    
    @Override
    public void paste(Vector3 origin, Chunk chunk) {
        // no-op
    }
    
    @Override
    public void paste(Vector3 origin, World world) {
        // no-op
    }
    
    @Override
    public Buffer addItem(BufferedItem item, Vector3 location) {
        item.paste(origin.clone().add(location), target);
        return this;
    }
    
    @Override
    public Buffer setMark(String mark, Vector3 location) {
        marks.put(location, mark);
        return this;
    }
    
    @Override
    public Vector3 getOrigin() {
        return origin;
    }
    
    @Override
    public String getMark(Vector3 location) {
        return marks.get(location);
    }
}
