package com.dfsek.terra.bukkit.world.block;

import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.block.MaterialData;

public class BukkitBlockData implements BlockData {
    private org.bukkit.block.data.BlockData delegate;

    public BukkitBlockData(org.bukkit.block.data.BlockData delegate) {
        this.delegate = delegate;
    }


    @Override
    public org.bukkit.block.data.BlockData getHandle() {
        return delegate;
    }

    @Override
    public MaterialData getMaterial() {
        return new BukkitMaterialData(delegate.getMaterial());
    }

    @Override
    public boolean matches(MaterialData materialData) {
        return delegate.getMaterial().equals(((BukkitMaterialData) materialData).getHandle());
    }

    @Override
    public BukkitBlockData clone() {
        try {
            BukkitBlockData n = (BukkitBlockData) super.clone();
            n.delegate = delegate.clone();
            return n;
        } catch(CloneNotSupportedException e) {
            throw new Error(e);
        }
    }
}
