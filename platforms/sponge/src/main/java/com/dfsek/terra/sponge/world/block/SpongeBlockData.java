package com.dfsek.terra.sponge.world.block;

import com.dfsek.terra.api.generic.world.block.BlockData;
import com.dfsek.terra.api.generic.world.block.MaterialData;
import org.spongepowered.api.block.BlockState;

public class SpongeBlockData implements BlockData {
    private final BlockState delegate;

    public SpongeBlockData(BlockState delegate) {
        this.delegate = delegate;
    }

    @Override
    public MaterialData getMaterial() {
        return new SpongeMaterialData(delegate.getType());
    }

    @Override
    public boolean matches(MaterialData materialData) {
        return ((SpongeMaterialData) materialData).getHandle().equals(delegate.getType());
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
