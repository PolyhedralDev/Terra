package com.dfsek.terra.api.implementations.bukkit.world.block.data;

import com.dfsek.terra.api.generic.world.block.data.Waterlogged;
import com.dfsek.terra.api.implementations.bukkit.world.block.BukkitBlockData;
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
