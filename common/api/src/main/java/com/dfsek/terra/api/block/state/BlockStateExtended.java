package com.dfsek.terra.api.block.state;

import com.dfsek.terra.api.data.ExtendedData;


public interface BlockStateExtended extends BlockState {
    /**
     * Gets the BlockData.
     *
     * @return BlockData of this BlockStateExtended
     */
    ExtendedData data();

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
    BlockState state();

    @Override
    default boolean extended() { return true; }
}
