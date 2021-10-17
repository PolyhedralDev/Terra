package com.dfsek.terra.addons.terrascript.buffer.items;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.block.entity.BlockEntity;
import com.dfsek.terra.api.structure.buffer.BufferedItem;
import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.api.world.World;


public class BufferedStateManipulator implements BufferedItem {
    private final Platform platform;
    private final String data;
    
    public BufferedStateManipulator(Platform platform, String state) {
        this.platform = platform;
        this.data = state;
    }
    
    @Override
    public void paste(Vector3 origin, World world) {
        try {
            BlockEntity state = world.getBlockState(origin);
            state.applyState(data);
            state.update(false);
        } catch(Exception e) {
            platform.logger().warning("Could not apply BlockState at " + origin + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}
