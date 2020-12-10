package com.dfsek.terra.api.bukkit.world.block.data;

import com.dfsek.terra.api.bukkit.world.block.BukkitBlockData;
import com.dfsek.terra.api.generic.world.block.data.Waterlogged;
import org.bukkit.block.data.BlockData;

public class BukkitWaterlogged extends BukkitBlockData implements Waterlogged {
    private boolean waterlogged;

    public BukkitWaterlogged(BlockData delegate) {
        super(delegate);
    }

    @Override
    public boolean isWaterlogged() {
        return waterlogged;
    }

    @Override
    public void setWaterlogged(boolean waterlogged) {
        this.waterlogged = waterlogged;
    }
}
