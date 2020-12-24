package com.dfsek.terra.api.structures.structure.buffer;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.math.vector.Vector3;

public interface Buffer {
    Buffer addItem(BufferedItem item, Vector3 location);

    Location getOrigin();
}
