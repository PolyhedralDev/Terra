package com.dfsek.terra.bukkit.world.block.data;

import com.dfsek.terra.api.platform.block.BlockFace;
import com.dfsek.terra.api.platform.block.data.Rotatable;
import com.dfsek.terra.bukkit.world.block.BukkitBlockData;

public class BukkitRotatable extends BukkitBlockData implements Rotatable {
    public BukkitRotatable(org.bukkit.block.data.Rotatable delegate) {
        super(delegate);
    }

    @Override
    public BlockFace getRotation() {
        return BukkitEnumAdapter.adapt(((org.bukkit.block.data.Rotatable) getHandle()).getRotation());
    }

    @Override
    public void setRotation(BlockFace face) {
        ((org.bukkit.block.data.Rotatable) getHandle()).setRotation(BukkitEnumAdapter.adapt(face));
    }
}
