package com.dfsek.terra.api.structures.structure.buffer;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.math.vector.Vector3;

import java.util.HashMap;
import java.util.Map;

public class StructureBuffer implements Buffer {
    private final Map<Vector3, BufferedItem> bufferedItemMap = new HashMap<>();
    private final Location origin;

    public StructureBuffer(Location origin) {
        this.origin = origin;
    }

    public void paste() {
        bufferedItemMap.forEach(((vector3, item) -> {
            item.paste(origin.clone().add(vector3));
        }));
    }

    @Override
    public Buffer addItem(BufferedItem item, Vector3 location) {
        bufferedItemMap.put(location, item);
        return this;
    }

    @Override
    public Location getOrigin() {
        return origin;
    }
}
