package com.dfsek.terra.bukkit.world.block.data;

import com.dfsek.terra.api.platform.block.data.Waterlogged;

public class BukkitWaterlogged extends BukkitBlockData implements Waterlogged {
    public BukkitWaterlogged(org.bukkit.block.data.Waterlogged delegate) {
        super(delegate);
    }

    @Override
    public boolean isWaterlogged() {
        return ((org.bukkit.block.data.Waterlogged) super.getHandle()).isWaterlogged();
    }

    @Override
    public void setWaterlogged(boolean waterlogged) {
        ((org.bukkit.block.data.Waterlogged) super.getHandle()).setWaterlogged(waterlogged);
    }
}
