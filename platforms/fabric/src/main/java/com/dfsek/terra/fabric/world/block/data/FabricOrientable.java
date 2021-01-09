package com.dfsek.terra.fabric.world.block.data;

import com.dfsek.terra.api.platform.block.Axis;
import com.dfsek.terra.api.platform.block.data.Orientable;
import com.dfsek.terra.fabric.world.block.FabricBlockData;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Properties;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class FabricOrientable extends FabricBlockData implements Orientable {
    public FabricOrientable(BlockState delegate) {
        super(delegate);
    }

    @Override
    public Set<Axis> getAxes() {
        return Arrays.stream(Axis.values()).collect(Collectors.toSet());
    }

    @Override
    public Axis getAxis() {
        return FabricEnumAdapter.adapt(getHandle().get(Properties.AXIS));
    }

    @Override
    public void setAxis(Axis axis) {
        delegate = delegate.with(Properties.AXIS, FabricEnumAdapter.adapt(axis));
    }
}
