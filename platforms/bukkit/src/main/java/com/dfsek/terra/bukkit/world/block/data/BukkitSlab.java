package com.dfsek.terra.bukkit.world.block.data;

import com.dfsek.terra.api.generic.world.block.data.Slab;
import com.dfsek.terra.api.generic.world.block.data.Waterlogged;
import com.dfsek.terra.bukkit.world.block.BukkitBlockData;
import org.bukkit.block.data.BlockData;

public class BukkitSlab extends BukkitBlockData implements Slab {
    public BukkitSlab(BlockData delegate) {
        super(delegate);
    }

    @Override
    public boolean isWaterlogged() {
        return ((Waterlogged) getHandle()).isWaterlogged();
    }

    @Override
    public void setWaterlogged(boolean waterlogged) {
        ((Waterlogged) getHandle()).setWaterlogged(waterlogged);
    }

    @Override
    public Type getType() {
        return BukkitEnumAdapter.fromBukkitSlabType(((org.bukkit.block.data.type.Slab) getHandle()).getType());
    }

    @Override
    public void setType(Type type) {
        ((org.bukkit.block.data.type.Slab) getHandle()).setType(TerraEnumAdapter.fromTerraSlabType(type));
    }
}
