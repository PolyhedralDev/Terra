package com.dfsek.terra.fabric.world.block;

import com.dfsek.terra.api.generic.world.block.BlockData;
import com.dfsek.terra.api.generic.world.block.MaterialData;
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
            return (BlockData) super.clone();
        } catch(CloneNotSupportedException e) {
            throw new Error(e);
        }
    }

    @Override
    public BlockState getHandle() {
        return delegate;
    }
}
