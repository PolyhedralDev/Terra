package com.dfsek.terra.api.gaea.world;

import com.dfsek.terra.api.gaea.math.Range;
import com.dfsek.terra.api.generic.world.Chunk;
import com.dfsek.terra.api.generic.world.block.Block;
import com.dfsek.terra.api.generic.world.vector.Location;

import java.util.List;

public interface Flora {
    List<Block> getValidSpawnsAt(Chunk chunk, int x, int z, Range check);

    boolean plant(Location l);
}
