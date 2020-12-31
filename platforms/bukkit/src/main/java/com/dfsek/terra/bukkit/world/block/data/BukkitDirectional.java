package com.dfsek.terra.bukkit.world.block.data;

import com.dfsek.terra.api.platform.block.BlockFace;
import com.dfsek.terra.api.platform.block.data.Directional;

public class BukkitDirectional extends BukkitBlockData implements Directional {
    public BukkitDirectional(org.bukkit.block.data.Directional delegate) {
        super(delegate);
    }

    @Override
    public BlockFace getFacing() {
        return BukkitEnumAdapter.adapt(((org.bukkit.block.data.Directional) getHandle()).getFacing());
    }

    @Override
    public void setFacing(BlockFace facing) {
        ((org.bukkit.block.data.Directional) getHandle()).setFacing(BukkitEnumAdapter.adapt(facing));
    }
}
