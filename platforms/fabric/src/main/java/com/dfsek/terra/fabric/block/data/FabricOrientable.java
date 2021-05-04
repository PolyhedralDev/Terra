package com.dfsek.terra.fabric.block.data;

import com.dfsek.terra.api.platform.block.Axis;
import com.dfsek.terra.api.platform.block.data.Orientable;
import com.dfsek.terra.fabric.block.FabricBlockData;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.math.Direction;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class FabricOrientable extends FabricBlockData implements Orientable {
    private final EnumProperty<Direction.Axis> property;

    public FabricOrientable(BlockState delegate, EnumProperty<Direction.Axis> property) {
        super(delegate);
        this.property = property;
    }

    @Override
    public Set<Axis> getAxes() {
        return Arrays.stream(Axis.values()).collect(Collectors.toSet());
    }

    @Override
    public Axis getAxis() {
        return FabricEnumAdapter.adapt(getHandle().get(property));
    }

    @Override
    public void setAxis(Axis axis) {
        delegate = delegate.with(property, FabricEnumAdapter.adapt(axis));
    }
}
