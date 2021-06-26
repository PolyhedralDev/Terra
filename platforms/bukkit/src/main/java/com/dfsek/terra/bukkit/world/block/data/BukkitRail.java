package com.dfsek.terra.bukkit.world.block.data;

import com.dfsek.terra.api.block.data.Rail;
import com.dfsek.terra.api.block.state.properties.enums.RailShape;
import com.dfsek.terra.bukkit.world.BukkitAdapter;

public class BukkitRail extends BukkitBlockState implements Rail {
    public BukkitRail(org.bukkit.block.data.Rail delegate) {
        super(delegate);
    }

    @Override
    public RailShape getShape() {
        return BukkitAdapter.adapt(((org.bukkit.block.data.Rail) getHandle()).getShape());
    }

    @Override
    public void setShape(RailShape newShape) {
        ((org.bukkit.block.data.Rail) getHandle()).setShape(BukkitAdapter.adapt(newShape));
    }
}
