package com.dfsek.terra.api.block.data;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.block.state.properties.enums.RailShape;

public interface Rail extends BlockState {
    RailShape getShape();

    void setShape(RailShape newShape);

}
