package com.dfsek.terra.fabric.world.block;

import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.block.BlockType;
import net.minecraft.block.BlockState;

public class FabricBlockData implements BlockData {
    protected BlockState delegate;

    public FabricBlockData(BlockState delegate) {
        this.delegate = delegate;
    }

    @Override
    public BlockType getBlockType() {
        return null;
    }

    @Override
    public boolean matches(BlockData other) {
        return false;
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
    public boolean isAir() {
        return false;
    }

    @Override
    public BlockState getHandle() {
        return delegate;
    }
}
