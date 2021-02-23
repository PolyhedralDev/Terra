package com.dfsek.terra.fabric.world.block;

import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.block.BlockType;
import com.dfsek.terra.fabric.world.FabricAdapter;
import net.minecraft.block.Block;

public class FabricBlockType implements BlockType {
    private final Block delegate;

    public FabricBlockType(Block delegate) {
        this.delegate = delegate;
    }

    @Override
    public Block getHandle() {
        return delegate;
    }

    @Override
    public BlockData getDefaultData() {
        return FabricAdapter.adapt(delegate.getDefaultState());
    }

    @Override
    public boolean isSolid() {
        return delegate.getDefaultState().isOpaque();
    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof FabricBlockType)) return false;
        return ((FabricBlockType) obj).delegate == delegate;
    }
}
