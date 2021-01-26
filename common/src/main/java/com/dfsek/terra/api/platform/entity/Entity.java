package com.dfsek.terra.api.platform.entity;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.CommandSender;
import com.dfsek.terra.api.platform.Handle;

public interface Entity extends Handle, CommandSender {
    Location getLocation();
}
