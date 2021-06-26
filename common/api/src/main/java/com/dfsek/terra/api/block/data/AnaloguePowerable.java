package com.dfsek.terra.api.block.data;

import com.dfsek.terra.api.block.state.BlockState;

public interface AnaloguePowerable extends BlockState {
    int getMaximumPower();

    int getPower();

    void setPower(int power);
}
