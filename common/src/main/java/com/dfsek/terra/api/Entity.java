package com.dfsek.terra.api;

import com.dfsek.terra.api.generic.Handle;
import com.dfsek.terra.api.generic.world.vector.Location;

public interface Entity extends Handle {
    Location getLocation();
}
