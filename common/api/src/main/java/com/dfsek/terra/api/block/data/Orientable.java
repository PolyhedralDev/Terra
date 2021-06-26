package com.dfsek.terra.api.block.data;

import com.dfsek.terra.api.block.state.properties.enums.Axis;
import com.dfsek.terra.api.block.state.BlockState;

import java.util.Set;

public interface Orientable extends BlockState {
    Set<Axis> getAxes();

    Axis getAxis();

    void setAxis(Axis axis);
}
