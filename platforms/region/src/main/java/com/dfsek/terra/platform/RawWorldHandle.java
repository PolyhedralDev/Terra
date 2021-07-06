package com.dfsek.terra.platform;

import com.dfsek.terra.api.block.BlockData;
import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.handle.WorldHandle;

public class RawWorldHandle implements WorldHandle {

    @Override
    public BlockData createBlockData(String data) {
        return new Data(data);
    }

    @Override
    public EntityType getEntity(String id) {
        return null;
    }
}
