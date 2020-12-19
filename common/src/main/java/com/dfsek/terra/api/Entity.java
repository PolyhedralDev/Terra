package com.dfsek.terra.api;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.Handle;

public interface Entity extends Handle {
    Location getLocation();
}
