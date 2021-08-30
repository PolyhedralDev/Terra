package com.dfsek.terra.forge.block.data;

import com.dfsek.terra.api.block.data.Slab;

import net.minecraft.block.BlockState;
import net.minecraft.state.properties.BlockStateProperties;


public class ForgeSlab extends ForgeWaterlogged implements Slab {
    public ForgeSlab(BlockState delegate) {
        super(delegate);
    }
    
    @Override
    public Type getType() {
        return ForgeEnumAdapter.adapt(delegate.getValue(BlockStateProperties.SLAB_TYPE));
    }
    
    @Override
    public void setType(Type type) {
        delegate = delegate.setValue(BlockStateProperties.SLAB_TYPE, ForgeEnumAdapter.adapt(type));
    }
}
