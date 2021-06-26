package com.dfsek.terra.bukkit.world.block.data;

import com.dfsek.terra.api.block.BlockFace;
import com.dfsek.terra.api.block.data.Rotatable;
import com.dfsek.terra.bukkit.world.BukkitAdapter;

public class BukkitRotatable extends BukkitBlockState implements Rotatable {
    public BukkitRotatable(org.bukkit.block.data.Rotatable delegate) {
        super(delegate);
    }

    @Override
    public BlockFace getRotation() {
        return BukkitAdapter.adapt(((org.bukkit.block.data.Rotatable) getHandle()).getRotation());
    }

    @Override
    public void setRotation(BlockFace face) {
        ((org.bukkit.block.data.Rotatable) getHandle()).setRotation(BukkitAdapter.adapt(face));
    }
}
