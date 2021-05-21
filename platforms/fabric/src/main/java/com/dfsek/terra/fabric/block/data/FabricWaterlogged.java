package com.dfsek.terra.fabric.block.data;

import com.dfsek.terra.api.platform.block.data.Waterlogged;
import com.dfsek.terra.fabric.block.FabricBlockData;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Properties;

public class FabricWaterlogged extends FabricBlockData implements Waterlogged {
    public FabricWaterlogged(BlockState delegate) {
        super(delegate);
    }

    @Override
    public boolean isWaterlogged() {
        return delegate.get(Properties.WATERLOGGED);
    }

    @Override
    public void setWaterlogged(boolean waterlogged) {
        super.delegate = delegate.with(Properties.WATERLOGGED, waterlogged);
    }
}
