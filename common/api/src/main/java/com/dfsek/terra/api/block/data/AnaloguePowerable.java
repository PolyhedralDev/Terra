package com.dfsek.terra.api.block.data;

import com.dfsek.terra.api.block.BlockData;

public interface AnaloguePowerable extends BlockData {
    int getMaximumPower();

    int getPower();

    void setPower(int power);
}
