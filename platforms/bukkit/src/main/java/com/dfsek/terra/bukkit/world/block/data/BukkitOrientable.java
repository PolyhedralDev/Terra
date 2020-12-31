package com.dfsek.terra.bukkit.world.block.data;

import com.dfsek.terra.api.platform.block.Axis;
import com.dfsek.terra.api.platform.block.data.Orientable;

import java.util.Set;
import java.util.stream.Collectors;

public class BukkitOrientable extends BukkitBlockData implements Orientable {

    public BukkitOrientable(org.bukkit.block.data.Orientable delegate) {
        super(delegate);
    }

    @Override
    public Set<Axis> getAxes() {
        return ((org.bukkit.block.data.Orientable) getHandle()).getAxes().stream().map(BukkitEnumAdapter::adapt).collect(Collectors.toSet());
    }

    @Override
    public Axis getAxis() {
        return BukkitEnumAdapter.adapt(((org.bukkit.block.data.Orientable) getHandle()).getAxis());
    }

    @Override
    public void setAxis(Axis axis) {
        ((org.bukkit.block.data.Orientable) getHandle()).setAxis(BukkitEnumAdapter.adapt(axis));
    }
}
