package com.dfsek.terra.bukkit.world.block.data;

import com.dfsek.terra.api.block.data.Slab;
import com.dfsek.terra.bukkit.world.BukkitAdapter;

public class BukkitSlab extends BukkitWaterlogged implements Slab {
    public BukkitSlab(org.bukkit.block.data.type.Slab delegate) {
        super(delegate);
    }

    @Override
    public Type getType() {
        return BukkitAdapter.adapt(((org.bukkit.block.data.type.Slab) getHandle()).getType());
    }

    @Override
    public void setType(Type type) {
        ((org.bukkit.block.data.type.Slab) getHandle()).setType(BukkitAdapter.adapt(type));
    }
}
