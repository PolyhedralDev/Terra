package com.dfsek.terra.api.entity;

import com.dfsek.terra.api.Handle;
import com.dfsek.terra.api.vector.Vector3;
import com.dfsek.terra.api.world.World;

public interface Entity extends Handle, CommandSender {
    Vector3 position();

    void position(Vector3 position);

    void world(World world);

    World world();
}
