package com.dfsek.terra.api.world.flora;

import com.dfsek.terra.api.math.Range;
import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.block.Block;
import com.dfsek.terra.api.platform.world.Chunk;

import java.util.List;

public interface Flora {
    List<Block> getValidSpawnsAt(Chunk chunk, int x, int z, Range check);

    boolean plant(Location l);
}
