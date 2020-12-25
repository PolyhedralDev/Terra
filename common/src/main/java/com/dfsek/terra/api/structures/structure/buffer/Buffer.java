package com.dfsek.terra.api.structures.structure.buffer;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.math.vector.Vector3;
import com.dfsek.terra.api.structures.structure.buffer.items.BufferedItem;
import com.dfsek.terra.api.structures.structure.buffer.items.Mark;

public interface Buffer {
    Buffer addItem(BufferedItem item, Vector3 location);

    Location getOrigin();

    Mark getMark(Vector3 location);

    Buffer setMark(Mark mark, Vector3 location);
}
