package com.dfsek.terra.bukkit.world.block;

import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.block.MaterialData;
import com.dfsek.terra.bukkit.world.block.data.BukkitBlockData;
import org.bukkit.Material;

public class BukkitMaterialData implements MaterialData {
    private final Material delegate;

    public BukkitMaterialData(Material delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean matches(MaterialData other) {
        return ((BukkitMaterialData) other).getHandle().equals(delegate);
    }

    @Override
    public boolean matches(BlockData other) {
        return ((BukkitMaterialData) other.getMaterial()).getHandle().equals(delegate);
    }

    @Override
    public boolean isSolid() {
        return delegate.isSolid();
    }

    @Override
    public boolean isAir() {
        return delegate.isAir();
    }

    @Override
    public double getMaxDurability() {
        return delegate.getMaxDurability();
    }

    @Override
    public BlockData createBlockData() {
        return BukkitBlockData.newInstance(delegate.createBlockData());
    }

    @Override
    public Material getHandle() {
        return delegate;
    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof BukkitMaterialData)) return false;
        BukkitMaterialData other = (BukkitMaterialData) obj;

        return other.getHandle().equals(this.delegate);
    }
}
