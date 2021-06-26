package com.dfsek.terra.api.block.data;

import com.dfsek.terra.api.block.Axis;
import com.dfsek.terra.api.block.BlockState;

import java.util.Set;

public interface Orientable extends BlockState {
    Set<Axis> getAxes();

    Axis getAxis();

    void setAxis(Axis axis);
}
