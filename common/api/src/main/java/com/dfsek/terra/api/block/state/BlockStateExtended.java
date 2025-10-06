package com.dfsek.terra.api.block.state;

import com.dfsek.terra.api.data.ExtendedData;


public interface BlockStateExtended extends BlockState {
    /**
     * Gets the BlockData.
     *
     * @return BlockData of this BlockStateExtended
     */
    ExtendedData getData();

    /**
     * Sets the BlockData.
     *
     * @param data BlockData to set
     *
     * @return New BlockStateExtended with the given BlockData
     */
    BlockStateExtended setData(ExtendedData data);

    /**
     * Gets the BlockState.
     *
     * @return Raw BlockState of this BlockStateExtended
     */
    BlockState getState();

    @Override
    default boolean isExtended() { return true; }
}
