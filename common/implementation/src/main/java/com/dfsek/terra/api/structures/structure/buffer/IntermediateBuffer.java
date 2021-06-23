package com.dfsek.terra.api.structures.structure.buffer;

import com.dfsek.terra.api.structure.buffer.Buffer;
import com.dfsek.terra.vector.LocationImpl;
import com.dfsek.terra.api.vector.Vector3;
import com.dfsek.terra.api.structure.buffer.BufferedItem;

public class IntermediateBuffer implements Buffer {
    private final Buffer original;
    private final Vector3 offset;

    public IntermediateBuffer(Buffer original, Vector3 offset) {
        this.original = original;
        this.offset = offset.clone();
    }

    @Override
    public Buffer addItem(BufferedItem item, LocationImpl location) {
        return original.addItem(item, location.add(offset));
    }

    @Override
    public LocationImpl getOrigin() {
        return original.getOrigin().clone().add(offset);
    }

    @Override
    public String getMark(LocationImpl location) {
        return original.getMark(location.add(offset));
    }

    @Override
    public Buffer setMark(String mark, LocationImpl location) {
        original.setMark(mark, location.add(offset));
        return this;
    }
}
