package com.dfsek.terra.fabric.world.block.data;

import com.dfsek.terra.api.generic.world.block.data.Slab;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Properties;

public class FabricSlab extends FabricWaterlogged implements Slab {
    public FabricSlab(BlockState delegate) {
        super(delegate);
    }

    @Override
    public Type getType() {
        return FabricEnumAdapter.fromFabricSlabType(delegate.get(Properties.SLAB_TYPE));
    }

    @Override
    public void setType(Type type) {
        delegate = delegate.with(Properties.SLAB_TYPE, TerraEnumAdapter.fromTerraSlabType(type));
    }
}
