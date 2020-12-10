package com.dfsek.terra.api.generic;

import com.dfsek.terra.api.generic.world.WorldHandle;

public interface TerraPlugin {
    WorldHandle getHandle();

    boolean isEnabled();
}
