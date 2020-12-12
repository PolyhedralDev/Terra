package com.dfsek.terra.bukkit.world.block.data;

import com.dfsek.terra.api.generic.world.block.data.Waterlogged;
import com.dfsek.terra.bukkit.world.block.BukkitBlockData;

public class BukkitWaterlogged extends BukkitBlockData implements Waterlogged {
    private boolean waterlogged;

    public BukkitWaterlogged(org.bukkit.block.data.Waterlogged delegate) {
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
