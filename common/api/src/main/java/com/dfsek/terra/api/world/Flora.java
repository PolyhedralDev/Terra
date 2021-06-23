package com.dfsek.terra.api.world;

import com.dfsek.terra.api.math.Range;
import com.dfsek.terra.api.vector.Location;
import com.dfsek.terra.api.block.Block;
import com.dfsek.terra.api.world.Chunk;

import java.util.List;

public interface Flora {
    List<Block> getValidSpawnsAt(Chunk chunk, int x, int z, Range check);

    boolean plant(Location l);
}
