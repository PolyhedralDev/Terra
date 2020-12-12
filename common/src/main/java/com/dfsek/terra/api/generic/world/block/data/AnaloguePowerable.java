package com.dfsek.terra.api.generic.world.block.data;

import com.dfsek.terra.api.generic.world.block.BlockData;

public interface AnaloguePowerable extends BlockData {
    int getMaximumPower();
    int getPower();
    void setPower(int power);
}
