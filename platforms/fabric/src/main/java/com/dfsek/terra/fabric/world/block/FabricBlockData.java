package com.dfsek.terra.fabric.world.block;

import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.block.MaterialData;
import net.minecraft.block.BlockState;

public class FabricBlockData implements BlockData {
    protected BlockState delegate;

    public FabricBlockData(BlockState delegate) {
        this.delegate = delegate;
    }

    @Override
    public MaterialData getMaterial() {
        return new FabricMaterialData(delegate.getBlock());
    }

    @Override
    public boolean matches(MaterialData materialData) {
        return ((FabricMaterialData) materialData).getHandle().is(delegate.getBlock());
    }

    @Override
    public BlockData clone() {
        try {
            return (FabricBlockData) super.clone();
        } catch(CloneNotSupportedException e) {
            throw new Error(e);
        }
    }

    @Override
    public String getAsString() {
        return delegate.toString();
    }

    @Override
    public BlockState getHandle() {
        return delegate;
    }
}
