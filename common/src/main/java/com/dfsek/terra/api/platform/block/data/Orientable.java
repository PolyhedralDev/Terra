package com.dfsek.terra.api.platform.block.data;

import com.dfsek.terra.api.platform.block.Axis;
import com.dfsek.terra.api.platform.block.BlockData;

import java.util.Set;

public interface Orientable extends BlockData {
    Set<Axis> getAxes();

    Axis getAxis();

    void setAxis(Axis axis);
}
