package com.dfsek.terra.forge.world.block;

import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.block.BlockType;
import com.dfsek.terra.forge.world.ForgeAdapter;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

public class ForgeBlockType implements BlockType {
    private final Block delegate;

    public ForgeBlockType(Block delegate) {
        this.delegate = delegate;
    }

    @Override
    public Block getHandle() {
        return delegate;
    }

    @Override
    public BlockData getDefaultData() {
        return ForgeAdapter.adapt(delegate.defaultBlockState());
    }

    @Override
    public boolean isSolid() {
        return delegate.defaultBlockState().canOcclude();
    }

    @Override
    public boolean isWater() {
        return delegate == Blocks.WATER;
    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof ForgeBlockType)) return false;
        return ((ForgeBlockType) obj).delegate == delegate;
    }
}
