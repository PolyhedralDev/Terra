package com.dfsek.terra.api.structure.buffer;

import com.dfsek.terra.api.vector.Location;
import com.dfsek.terra.api.world.Chunk;

public interface BufferedItem {
    void paste(Location origin);

    default void paste(Chunk chunk, Location origin) {
        origin.setWorld(chunk.getWorld()); // Fabric weirdness
        paste(origin);
    }
}
