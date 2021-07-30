package com.dfsek.terra.sponge.handle;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.handle.WorldHandle;
import com.dfsek.terra.sponge.block.SpongeBlockState;
import org.spongepowered.api.block.BlockTypes;

public class SpongeWorldHandle implements WorldHandle {
    private final SpongeBlockState air;

    public SpongeWorldHandle() {
        air = new SpongeBlockState(BlockTypes.AIR.get().defaultState());
    }

    @Override
    public BlockState createBlockData(String data) {
        return new SpongeBlockState(org.spongepowered.api.block.BlockState.fromString(data));
    }

    @Override
    public BlockState air() {
        return air;
    }

    @Override
    public EntityType getEntity(String id) {
        return null;
    }
}
