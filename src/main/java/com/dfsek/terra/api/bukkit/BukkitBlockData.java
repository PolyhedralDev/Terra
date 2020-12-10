package com.dfsek.terra.api.bukkit;

import com.dfsek.terra.api.generic.BlockData;

public class BukkitBlockData implements BlockData {
    private final org.bukkit.block.data.BlockData delegate;

    public BukkitBlockData(org.bukkit.block.data.BlockData delegate) {
        this.delegate = delegate;
    }


    @Override
    public org.bukkit.block.data.BlockData getHandle() {
        return delegate;
    }
}
