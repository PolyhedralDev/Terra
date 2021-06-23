package com.dfsek.terra.api.structures.structure.buffer;

import com.dfsek.terra.api.vector.Location;
import com.dfsek.terra.api.vector.Vector3;
import com.dfsek.terra.api.structures.structure.buffer.items.BufferedItem;

public class IntermediateBuffer implements Buffer {
    private final Buffer original;
    private final Vector3 offset;

    public IntermediateBuffer(Buffer original, Vector3 offset) {
        this.original = original;
        this.offset = offset.clone();
    }

    @Override
    public Buffer addItem(BufferedItem item, Location location) {
        return original.addItem(item, location.add(offset));
    }

    @Override
    public Location getOrigin() {
        return original.getOrigin().clone().add(offset);
    }

    @Override
    public String getMark(Location location) {
        return original.getMark(location.add(offset));
    }

    @Override
    public Buffer setMark(String mark, Location location) {
        original.setMark(mark, location.add(offset));
        return this;
    }
}
