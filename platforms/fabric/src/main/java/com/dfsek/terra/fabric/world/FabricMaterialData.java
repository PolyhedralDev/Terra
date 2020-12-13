package com.dfsek.terra.fabric.world;

import com.dfsek.terra.api.generic.world.block.BlockData;
import com.dfsek.terra.api.generic.world.block.MaterialData;
import net.minecraft.block.Material;

public class FabricMaterialData implements MaterialData {
    private final Material delegate;

    public FabricMaterialData(Material delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean matches(MaterialData other) {
        return delegate.equals(((FabricMaterialData) other).getHandle());
    }

    @Override
    public boolean matches(BlockData other) {
        return delegate.equals(((FabricMaterialData) other.getMaterial()).getHandle());
    }

    @Override
    public boolean isSolid() {
        return delegate.isSolid();
    }

    @Override
    public boolean isAir() {
        return delegate.blocksMovement(); // TODO: better impl
    }

    @Override
    public double getMaxDurability() {
        return 0;
    }

    @Override
    public BlockData createBlockData() {
        return null;
    }

    @Override
    public Material getHandle() {
        return delegate;
    }
}
