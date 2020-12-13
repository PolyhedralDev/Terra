package com.dfsek.terra.fabric.world;

import com.dfsek.terra.api.generic.world.block.BlockData;
import com.dfsek.terra.api.generic.world.block.MaterialData;
import net.minecraft.block.BlockState;

public class FabricBlockData implements BlockData {
    private final BlockState delegate;

    public FabricBlockData(BlockState delegate) {
        this.delegate = delegate;
    }

    @Override
    public MaterialData getMaterial() {
        return null;
    }

    @Override
    public boolean matches(MaterialData materialData) {
        return false;
    }

    @Override
    public BlockData clone() {
        return null;
    }

    @Override
    public BlockState getHandle() {
        return delegate;
    }
}
