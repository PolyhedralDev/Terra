package com.dfsek.terra.api.block.data;

import com.dfsek.terra.api.block.state.BlockState;

public interface Bisected extends BlockState {
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
