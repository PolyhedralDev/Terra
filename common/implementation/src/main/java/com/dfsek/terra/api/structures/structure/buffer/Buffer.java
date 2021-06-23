package com.dfsek.terra.api.structures.structure.buffer;

import com.dfsek.terra.api.vector.Location;
import com.dfsek.terra.api.structures.structure.buffer.items.BufferedItem;

public interface Buffer {
    Buffer addItem(BufferedItem item, Location location);

    Location getOrigin();

    String getMark(Location location);

    Buffer setMark(String mark, Location location);
}
