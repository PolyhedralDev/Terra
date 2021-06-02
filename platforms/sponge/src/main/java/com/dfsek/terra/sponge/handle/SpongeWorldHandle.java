package com.dfsek.terra.sponge.handle;

import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.entity.EntityType;
import com.dfsek.terra.api.platform.handle.WorldHandle;
import com.dfsek.terra.sponge.SpongeAdapter;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockState;

public class SpongeWorldHandle implements WorldHandle {
    @Override
    public BlockData createBlockData(String data) {
        return SpongeAdapter.adapt(BlockState.fromString(data));
    }

    @Override
    public EntityType getEntity(String id) {
        return null;
    }
}
