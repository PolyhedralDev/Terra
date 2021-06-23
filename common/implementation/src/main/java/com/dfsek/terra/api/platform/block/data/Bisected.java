package com.dfsek.terra.api.platform.block.data;

import com.dfsek.terra.api.platform.block.BlockData;

public interface Bisected extends BlockData {
    Half getHalf();

    void setHalf(Half half);

    enum Half {
        /**
         * The top half of the block, normally with the higher y coordinate.
         */
        TOP,
        /**
         * The bottom half of the block, normally with the lower y coordinate.
         */
        BOTTOM
    }
}
