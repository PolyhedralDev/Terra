package com.dfsek.terra.fabric.world.handles.world;

import com.dfsek.terra.api.platform.world.World;
import net.minecraft.world.WorldAccess;

public interface FabricWorldHandle extends World {
    WorldAccess getWorld();
}
