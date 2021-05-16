package com.dfsek.terra.bukkit.world.block.data;

import com.dfsek.terra.api.platform.block.BlockFace;
import com.dfsek.terra.api.platform.block.data.Wall;
import com.dfsek.terra.bukkit.world.BukkitAdapter;

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

    public static org.bukkit.block.data.type.Wall.Height adapt(com.dfsek.terra.api.platform.block.data.Wall.Height height) {
        return switch(height) {
            case NONE -> org.bukkit.block.data.type.Wall.Height.NONE;
            case LOW -> org.bukkit.block.data.type.Wall.Height.LOW;
            case TALL -> org.bukkit.block.data.type.Wall.Height.TALL;
        };
    }

    public static com.dfsek.terra.api.platform.block.data.Wall.Height adapt(org.bukkit.block.data.type.Wall.Height height) {
        return switch(height) {
            case TALL -> Height.TALL;
            case LOW -> Height.LOW;
            case NONE -> Height.NONE;
        };
    }

    @Override
    public void setHeight(BlockFace face, Height height) {
        ((org.bukkit.block.data.type.Wall) getHandle()).setHeight(BukkitAdapter.adapt(face), adapt(height));
    }

    @Override
    public Height getHeight(BlockFace face) {
        return adapt(((org.bukkit.block.data.type.Wall) getHandle()).getHeight(BukkitAdapter.adapt(face)));
    }
}
