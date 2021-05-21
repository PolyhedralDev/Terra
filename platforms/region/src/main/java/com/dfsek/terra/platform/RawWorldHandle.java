package com.dfsek.terra.platform;

import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.entity.EntityType;
import com.dfsek.terra.api.platform.handle.WorldHandle;

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
