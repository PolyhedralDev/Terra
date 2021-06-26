package com.dfsek.terra.sponge.world;

import com.dfsek.terra.api.block.BlockState;
import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.handle.WorldHandle;
import com.dfsek.terra.sponge.SpongeAdapter;
import org.spongepowered.api.Sponge;

public class SpongeWorldHandle implements WorldHandle {
    @Override
    public BlockState createBlockData(String data) {
        return SpongeAdapter.adapt(Sponge.getRegistry().getType(org.spongepowered.api.block.BlockState.class, data).orElseThrow(() -> new IllegalArgumentException("Invalid block data \"" + data + "\"")));
    }

    @Override
    public EntityType getEntity(String id) {
        return null;
    }
}
