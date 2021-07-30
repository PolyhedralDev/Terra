package com.dfsek.terra.sponge.block;

import com.dfsek.terra.api.block.BlockType;
import com.dfsek.terra.api.block.state.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.Keys;

public class SpongeBlockType implements BlockType {
    private final org.spongepowered.api.block.BlockType delegate;

    public SpongeBlockType(org.spongepowered.api.block.BlockType delegate) {
        this.delegate = delegate;
    }

    @Override
    public org.spongepowered.api.block.BlockType getHandle() {
        return delegate;
    }

    @Override
    public BlockState getDefaultData() {
        return new SpongeBlockState(delegate.defaultState());
    }

    @Override
    public boolean isSolid() {
        return !delegate.getOrElse(Keys.IS_SOLID, false);
    }

    @Override
    public boolean isWater() {
        return delegate.equals(BlockTypes.WATER.get());
    }
}
