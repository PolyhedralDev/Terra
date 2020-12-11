package com.dfsek.terra.api.generic;

import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.api.LoaderRegistrar;
import com.dfsek.terra.api.generic.world.World;
import com.dfsek.terra.api.generic.world.WorldHandle;

import java.util.logging.Logger;

public interface TerraPlugin extends LoaderRegistrar {
    WorldHandle getHandle();

    boolean isEnabled();

    TerraWorld getWorld(World world);

    Logger getLogger();
}
