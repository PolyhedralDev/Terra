package com.dfsek.terra.api.world;

import com.dfsek.terra.api.util.Range;
import com.dfsek.terra.api.vector.Location;
import com.dfsek.terra.api.vector.Vector3;

import java.util.List;

public interface Flora {
    List<Vector3> getValidSpawnsAt(Chunk chunk, int x, int z, Range check);

    boolean plant(Location l);
}
