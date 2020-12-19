package com.dfsek.terra.api.platform.world.block.data;

import com.dfsek.terra.api.platform.world.block.Axis;
import com.dfsek.terra.api.platform.world.block.BlockData;

import java.util.Set;

public interface Orientable extends BlockData {
    Set<Axis> getAxes();

    Axis getAxis();

    void setAxis(Axis axis);
}
