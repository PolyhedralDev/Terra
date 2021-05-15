package com.dfsek.terra.forge.block.data;

import com.dfsek.terra.api.platform.block.data.Waterlogged;
import com.dfsek.terra.forge.block.ForgeBlockData;
import net.minecraft.block.BlockState;
import net.minecraft.state.properties.BlockStateProperties;

public class ForgeWaterlogged extends ForgeBlockData implements Waterlogged {
    public ForgeWaterlogged(BlockState delegate) {
        super(delegate);
    }

    @Override
    public boolean isWaterlogged() {
        return delegate.getValue(BlockStateProperties.WATERLOGGED);
    }

    @Override
    public void setWaterlogged(boolean waterlogged) {
        super.delegate = delegate.setValue(BlockStateProperties.WATERLOGGED, waterlogged);
    }
}
