package com.dfsek.terra.fabric.world.block;

import com.dfsek.terra.api.platform.world.block.BlockData;
import com.dfsek.terra.api.platform.world.block.MaterialData;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

public class FabricMaterialData implements MaterialData {
    private final Block delegate;

    public FabricMaterialData(Block delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean matches(MaterialData other) {
        return delegate.equals(((FabricMaterialData) other).getHandle());
    }

    @Override
    public boolean matches(BlockData other) {
        return delegate.equals(((FabricMaterialData) other.getMaterial()).getHandle());
    }

    @Override
    public boolean isSolid() {
        return !delegate.is(Blocks.AIR);
    }

    @Override
    public boolean isAir() {
        return delegate.is(Blocks.AIR); // TODO: better impl
    }

    @Override
    public double getMaxDurability() {
        return 0;
    }

    @Override
    public BlockData createBlockData() {
        return new FabricBlockData(delegate.getDefaultState());
    }

    @Override
    public Block getHandle() {
        return delegate;
    }


    @Override
    public int hashCode() {
        return delegate.asItem().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof FabricMaterialData) {
            return ((FabricMaterialData) obj).matches(this);
        }
        return false;
    }
}
