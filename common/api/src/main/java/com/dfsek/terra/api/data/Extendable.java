package com.dfsek.terra.api.data;

public interface Extendable {
    /**
     * Get whether this BlockState is an extended state.
     * Extended states are states that contain extra data not normally present in a BlockState.
     *
     * @return Whether this state is extended.
     */
    default boolean isExtended() { return false; }
}
