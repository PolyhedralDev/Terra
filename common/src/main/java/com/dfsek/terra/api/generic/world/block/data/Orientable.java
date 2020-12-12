package com.dfsek.terra.api.generic.world.block.data;

import com.dfsek.terra.api.generic.world.block.Axis;
import com.dfsek.terra.api.generic.world.block.BlockData;

import java.util.Set;

public interface Orientable extends BlockData {
    Set<Axis> getAxes();
    void setAxis(Axis axis);
    Axis getAxis();
}
