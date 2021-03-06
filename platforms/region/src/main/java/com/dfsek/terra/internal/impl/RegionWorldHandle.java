package com.dfsek.terra.internal.impl;

import com.dfsek.terra.api.platform.block.Block;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.entity.EntityType;
import com.dfsek.terra.api.platform.handle.WorldHandle;
import com.dfsek.terra.internal.InternalData;

import java.util.HashMap;
import java.util.Map;

public class RegionWorldHandle implements WorldHandle {
    private final Map<String, InternalData> dataMap;

    public RegionWorldHandle() {
        this.dataMap = new HashMap<>();
    }

    @Override

    public void setBlockData(Block block, BlockData data, boolean physics) {
        block.setBlockData(data, physics);
    }

    @Override
    public BlockData getBlockData(Block block) {
        return block.getBlockData();
    }

    @Override
    public BlockData createBlockData(String data) {
        return dataMap.computeIfAbsent(data, RegionData::new);
    }

    @Override
    public EntityType getEntity(String id) {
        return null;
    }

}
