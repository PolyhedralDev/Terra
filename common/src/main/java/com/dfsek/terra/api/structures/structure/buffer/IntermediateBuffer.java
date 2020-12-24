package com.dfsek.terra.api.structures.structure.buffer;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.math.vector.Vector3;

public class IntermediateBuffer implements Buffer {
    private final Buffer original;
    private final Vector3 offset;

    public IntermediateBuffer(Buffer original, Vector3 offset) {
        this.original = original;
        this.offset = offset;
    }

    @Override
    public Buffer addItem(BufferedItem item, Vector3 location) {
        return original.addItem(item, location.add(offset));
    }

    @Override
    public Location getOrigin() {
        return original.getOrigin().add(offset);
    }
}
