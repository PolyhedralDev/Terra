package com.dfsek.terra.sponge.world.block;

import com.dfsek.terra.api.generic.world.block.BlockData;
import com.dfsek.terra.api.generic.world.block.MaterialData;
import org.spongepowered.api.block.BlockType;

public class SpongeMaterialData implements MaterialData {
    private final BlockType delegate;

    public SpongeMaterialData(BlockType delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean matches(MaterialData other) {
        return delegate.equals(((SpongeMaterialData) other).getHandle());
    }

    @Override
    public boolean matches(BlockData other) {
        return false;
    }

    @Override
    public boolean isSolid() {
        return false;
    }

    @Override
    public boolean isAir() {
        return false;
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
    public BlockType getHandle() {
        return delegate;
    }
}
