package com.dfsek.terra.bukkit.world.block.data;

import com.dfsek.terra.api.block.BlockFace;
import com.dfsek.terra.api.block.data.Directional;
import com.dfsek.terra.bukkit.world.BukkitAdapter;

public class BukkitDirectional extends BukkitBlockData implements Directional {
    public BukkitDirectional(org.bukkit.block.data.Directional delegate) {
        super(delegate);
    }

    @Override
    public BlockFace getFacing() {
        return BukkitAdapter.adapt(((org.bukkit.block.data.Directional) getHandle()).getFacing());
    }

    @Override
    public void setFacing(BlockFace facing) {
        ((org.bukkit.block.data.Directional) getHandle()).setFacing(BukkitAdapter.adapt(facing));
    }
}
