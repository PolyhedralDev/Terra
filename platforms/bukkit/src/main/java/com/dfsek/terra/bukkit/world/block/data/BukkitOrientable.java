package com.dfsek.terra.bukkit.world.block.data;

import com.dfsek.terra.api.block.state.properties.enums.Axis;
import com.dfsek.terra.api.block.data.Orientable;
import com.dfsek.terra.bukkit.world.BukkitAdapter;

import java.util.Set;
import java.util.stream.Collectors;

public class BukkitOrientable extends BukkitBlockState implements Orientable {

    public BukkitOrientable(org.bukkit.block.data.Orientable delegate) {
        super(delegate);
    }

    @Override
    public Set<Axis> getAxes() {
        return ((org.bukkit.block.data.Orientable) getHandle()).getAxes().stream().map(BukkitAdapter::adapt).collect(Collectors.toSet());
    }

    @Override
    public Axis getAxis() {
        return BukkitAdapter.adapt(((org.bukkit.block.data.Orientable) getHandle()).getAxis());
    }

    @Override
    public void setAxis(Axis axis) {
        ((org.bukkit.block.data.Orientable) getHandle()).setAxis(BukkitAdapter.adapt(axis));
    }
}
