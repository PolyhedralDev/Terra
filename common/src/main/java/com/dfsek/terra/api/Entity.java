package com.dfsek.terra.api;

import com.dfsek.terra.api.platform.Handle;
import com.dfsek.terra.api.platform.world.vector.Location;

public interface Entity extends Handle {
    Location getLocation();
}
