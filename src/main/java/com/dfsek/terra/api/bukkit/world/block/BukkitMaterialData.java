package com.dfsek.terra.api.bukkit.world.block;

import com.dfsek.terra.api.generic.world.block.BlockData;
import com.dfsek.terra.api.generic.world.block.MaterialData;
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
    public Material getHandle() {
        return delegate;
    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }
}
