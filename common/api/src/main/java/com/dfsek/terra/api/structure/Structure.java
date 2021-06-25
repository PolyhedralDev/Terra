package com.dfsek.terra.api.structure;

import com.dfsek.terra.api.structure.buffer.Buffer;
import com.dfsek.terra.api.structure.rotation.Rotation;
import com.dfsek.terra.api.vector.Location;
import com.dfsek.terra.api.world.Chunk;
import com.dfsek.terra.api.world.World;

import java.util.Random;

public interface Structure {
    /**
     * Paste the structure at a location
     *
     * @param location Location to paste structure
     * @param rotation Rotation of structure
     * @return Whether generation was successful
     */
    @SuppressWarnings("try")
    boolean generate(Location location, Random random, Rotation rotation);

    @SuppressWarnings("try")
    boolean generate(Location location, Chunk chunk, Random random, Rotation rotation);

    @SuppressWarnings("try")
    boolean test(Location location, Random random, Rotation rotation);

    @SuppressWarnings("try")
    boolean generate(Buffer buffer, World world, Random random, Rotation rotation, int recursions);

    @SuppressWarnings("try")
    boolean generateDirect(Location location, Random random, Rotation rotation);

    String getId();
}
