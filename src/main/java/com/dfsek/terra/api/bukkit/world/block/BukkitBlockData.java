package com.dfsek.terra.api.bukkit.world.block;

import com.dfsek.terra.api.generic.world.block.BlockData;
import com.dfsek.terra.api.generic.world.block.MaterialData;

public class BukkitBlockData implements BlockData {
    private final org.bukkit.block.data.BlockData delegate;

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
            return (BukkitBlockData) super.clone();
        } catch(CloneNotSupportedException e) {
            throw new Error(e);
        }
    }
}
