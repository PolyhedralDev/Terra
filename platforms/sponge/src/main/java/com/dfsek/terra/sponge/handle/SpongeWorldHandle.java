package com.dfsek.terra.sponge.handle;

import org.spongepowered.api.block.BlockTypes;

import com.dfsek.terra.api.block.entity.BlockEntity;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.handle.WorldHandle;
import com.dfsek.terra.api.util.generic.Lazy;
import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.sponge.block.SpongeBlockState;


public class SpongeWorldHandle implements WorldHandle {
    private final Lazy<SpongeBlockState> air;
    
    public SpongeWorldHandle() {
        air = Lazy.lazy(() -> new SpongeBlockState(BlockTypes.AIR.get().defaultState()));
    }
    
    @Override
    public BlockState createBlockData(String data) {
        return new SpongeBlockState(org.spongepowered.api.block.BlockState.fromString(data));
    }
    
    @Override
    public BlockState air() {
        return air.value();
    }
    
    @Override
    public BlockEntity createBlockEntity(Vector3 location, BlockState block, String snbt) {
        return null;
    }
    
    @Override
    public EntityType getEntity(String id) {
        return null;
    }
}
