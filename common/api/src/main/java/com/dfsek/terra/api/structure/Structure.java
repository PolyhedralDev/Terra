package com.dfsek.terra.api.structure;

import com.dfsek.terra.api.StringIdentifiable;
import com.dfsek.terra.api.structure.buffer.Buffer;
import com.dfsek.terra.api.structure.rotation.Rotation;
import com.dfsek.terra.api.vector.Vector3;
import com.dfsek.terra.api.world.Chunk;
import com.dfsek.terra.api.world.World;

import java.util.Random;

public interface Structure extends StringIdentifiable {
    /**
     * Paste the structure at a location
     *
     * @param location Location to paste structure
     * @param world
     * @param rotation Rotation of structure
     * @return Whether generation was successful
     */
    @SuppressWarnings("try")
    boolean generate(Vector3 location, World world, Random random, Rotation rotation);

    @SuppressWarnings("try")
    boolean generate(Vector3 location, World world, Chunk chunk, Random random, Rotation rotation);

    @SuppressWarnings("try")
    boolean generate(Buffer buffer, World world, Random random, Rotation rotation, int recursions);

    @SuppressWarnings("try")
    boolean generateDirect(Vector3 location, World world, Random random, Rotation rotation);
}
