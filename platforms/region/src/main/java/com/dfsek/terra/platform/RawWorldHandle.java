package com.dfsek.terra.platform;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.handle.WorldHandle;

public class RawWorldHandle implements WorldHandle {

    @Override
    public BlockState createBlockData(String data) {
        return new State(data);
    }

    @Override
    public BlockState air() {
        return null;
    }

    @Override
    public EntityType getEntity(String id) {
        return null;
    }
}
