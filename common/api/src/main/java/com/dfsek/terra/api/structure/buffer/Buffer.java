package com.dfsek.terra.api.structure.buffer;

import com.dfsek.terra.api.vector.Location;

public interface Buffer {
    Buffer addItem(BufferedItem item, Location location);

    Location getOrigin();

    String getMark(Location location);

    Buffer setMark(String mark, Location location);
}
