package com.dfsek.terra.sponge.world.block.data;

import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.block.BlockType;
import org.spongepowered.api.block.BlockState;

public class SpongeBlockData implements BlockData {
    private final BlockState delegate;

    public SpongeBlockData(BlockState delegate) {
        this.delegate = delegate;
    }

    @Override
    public BlockState getHandle() {
        return delegate;
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
        return null;
    }

    @Override
    public String getAsString() {
        return null;
    }

    @Override
    public boolean isAir() {
        return false;
    }
}
