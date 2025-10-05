package com.dfsek.terra.api.block.state;

import com.dfsek.terra.api.block.BlockData;


public interface BlockStateExtended extends BlockState {
    /**
     * Sets the BlockData.
     *
     * @param data BlockData to set
     *
     * @return New BlockStateExtended with the given BlockData
     */
     BlockStateExtended setData(BlockData data);

    /**
     * Gets the BlockData.
     *
     * @return BlockData of this BlockStateExtended
     */
    BlockData getData();

    /**
     * Gets the BlockState.
     *
     * @return Raw BlockState of this BlockStateExtended
     */
    BlockState getState();

    @Override
    default boolean isExtended() {return true;}
}
