package com.dfsek.terra.api.platform.entity;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.CommandSender;
import com.dfsek.terra.api.platform.Handle;
import com.dfsek.terra.api.platform.world.World;

public interface Entity extends Handle, CommandSender {
    Location getLocation();

    void setLocation(Location location);

    World getWorld();
}
