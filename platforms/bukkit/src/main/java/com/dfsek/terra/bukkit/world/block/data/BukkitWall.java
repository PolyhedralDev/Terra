package com.dfsek.terra.bukkit.world.block.data;

import com.dfsek.terra.api.platform.block.BlockFace;
import com.dfsek.terra.api.platform.block.data.Wall;

public class BukkitWall extends BukkitWaterlogged implements Wall {
    public BukkitWall(org.bukkit.block.data.type.Wall delegate) {
        super(delegate);
    }

    @Override
    public boolean isUp() {
        return ((org.bukkit.block.data.type.Wall) getHandle()).isUp();
    }

    @Override
    public void setUp(boolean up) {
        ((org.bukkit.block.data.type.Wall) getHandle()).setUp(up);
    }

    @Override
    public void setHeight(BlockFace face, Height height) {
        ((org.bukkit.block.data.type.Wall) getHandle()).setHeight(BukkitEnumAdapter.adapt(face), BukkitEnumAdapter.adapt(height));
    }

    @Override
    public Height getHeight(BlockFace face) {
        return BukkitEnumAdapter.adapt(((org.bukkit.block.data.type.Wall) getHandle()).getHeight(BukkitEnumAdapter.adapt(face)));
    }
}
