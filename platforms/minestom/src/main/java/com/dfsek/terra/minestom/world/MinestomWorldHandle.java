package com.dfsek.terra.minestom.world;

import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.entity.EntityType;
import com.dfsek.terra.api.platform.handle.WorldHandle;
import net.minestom.server.instance.block.Block;

import java.util.HashMap;
import java.util.Map;

public class MinestomWorldHandle implements WorldHandle {
    private static final Map<String, Block> BLOCKS = new HashMap<>();

    static {
        for(Block value : Block.values()) {
            BLOCKS.put(value.getName(), value);
        }
    }

    @Override
    public BlockData createBlockData(String data) {
        String baseData = data.contains("[") ? data.substring(0, data.indexOf('[')) : data;

        Block minestomBlock = BLOCKS.get(baseData);
        if(data.contains("[")) {
            String properties = data.substring(data.indexOf('['), data.indexOf(']'));
            String[] propArray = properties.split(",");
            minestomBlock = Block.fromStateId(minestomBlock.withProperties(propArray));
        }

        return new MinestomBlockData(minestomBlock);
    }

    @Override
    public EntityType getEntity(String id) {
        return null;
    }
}
