package com.dfsek.terra.api.structure.buffer;

import com.dfsek.terra.api.vector.Vector3;
import com.dfsek.terra.api.world.World;

public interface BufferedItem {
    void paste(Vector3 origin, World world);
}
