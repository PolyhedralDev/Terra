package com.dfsek.terra.addons.terrascript.buffer;

import com.dfsek.terra.api.structure.buffer.Buffer;
import com.dfsek.terra.api.structure.buffer.BufferedItem;
import com.dfsek.terra.api.vector.Vector3;
import com.dfsek.terra.api.world.Chunk;
import com.dfsek.terra.api.world.World;

public class IntermediateBuffer implements Buffer {
    private final Buffer original;
    private final Vector3 offset;

    public IntermediateBuffer(Buffer original, Vector3 offset) {
        this.original = original;
        this.offset = offset.clone();
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
        return original.addItem(item, location.clone().add(offset));
    }

    @Override
    public Vector3 getOrigin() {
        return original.getOrigin().clone().add(offset);
    }

    @Override
    public String getMark(Vector3 location) {
        return original.getMark(location.clone().add(offset));
    }

    @Override
    public Buffer setMark(String mark, Vector3 location) {
        original.setMark(mark, location.clone().add(offset));
        return this;
    }
}
