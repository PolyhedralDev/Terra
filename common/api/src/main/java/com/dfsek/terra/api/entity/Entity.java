package com.dfsek.terra.api.entity;

import com.dfsek.terra.api.vector.Location;
import com.dfsek.terra.api.Handle;
import com.dfsek.terra.api.world.World;

public interface Entity extends Handle, CommandSender {
    Location getLocation();

    void setLocation(Location location);

    World getWorld();
}
