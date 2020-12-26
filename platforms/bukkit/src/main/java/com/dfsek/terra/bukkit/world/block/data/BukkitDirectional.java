package com.dfsek.terra.bukkit.world.block.data;

import com.dfsek.terra.api.platform.block.BlockFace;
import com.dfsek.terra.api.platform.block.data.Directional;
import com.dfsek.terra.bukkit.world.block.BukkitBlockData;

public class BukkitDirectional extends BukkitBlockData implements Directional {
    public BukkitDirectional(org.bukkit.block.data.Directional delegate) {
        super(delegate);
    }

    @Override
    public BlockFace getFacing() {
        return BukkitEnumAdapter.fromBukkitBlockFace(((org.bukkit.block.data.Directional) getHandle()).getFacing());
    }

    @Override
    public void setFacing(BlockFace facing) {
        ((org.bukkit.block.data.Directional) getHandle()).setFacing(TerraEnumAdapter.fromTerraBlockFace(facing));
    }
}
