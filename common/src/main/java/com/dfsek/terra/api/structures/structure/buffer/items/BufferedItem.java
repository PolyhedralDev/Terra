package com.dfsek.terra.api.structures.structure.buffer.items;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.world.Chunk;

public interface BufferedItem {
    void paste(Location origin);

    default void paste(Chunk chunk, Location origin) {
        origin.setWorld(chunk.getWorld()); // Fabric weirdness
        paste(origin);
    }
}
