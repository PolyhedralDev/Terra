package com.dfsek.terra.api.platform.block.data;

import com.dfsek.terra.api.platform.block.BlockData;

public interface AnaloguePowerable extends BlockData {
    int getMaximumPower();
    int getPower();
    void setPower(int power);
}
