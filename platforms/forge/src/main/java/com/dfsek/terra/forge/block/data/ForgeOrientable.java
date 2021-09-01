package com.dfsek.terra.forge.block.data;

import com.dfsek.terra.api.block.Axis;
import com.dfsek.terra.api.block.data.Orientable;
import com.dfsek.terra.forge.block.ForgeBlockData;

import net.minecraft.block.BlockState;
import net.minecraft.state.EnumProperty;
import net.minecraft.util.Direction;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;


public class ForgeOrientable extends ForgeBlockData implements Orientable {
    private final EnumProperty<Direction.Axis> property;
    
    public ForgeOrientable(BlockState delegate, EnumProperty<Direction.Axis> property) {
        super(delegate);
        this.property = property;
    }
    
    @Override
    public Set<Axis> getAxes() {
        return Arrays.stream(Axis.values()).collect(Collectors.toSet());
    }
    
    @Override
    public Axis getAxis() {
        return ForgeEnumAdapter.adapt(getHandle().getValue(property));
    }
    
    @Override
    public void setAxis(Axis axis) {
        delegate = delegate.setValue(property, ForgeEnumAdapter.adapt(axis));
    }
}
