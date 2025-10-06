package com.dfsek.terra.api.entity;

import com.dfsek.terra.api.data.ExtendedData;


public interface EntityTypeExtended extends EntityType {
    /**
     * Gets the BlockData.
     *
     * @return BlockData of this EntityTypeExtended
     */
    ExtendedData getData();

    /**
     * Sets the BlockData.
     *
     * @param data BlockData to set
     *
     * @return New EntityTypeExtended with the given BlockData
     */
    EntityTypeExtended setData(ExtendedData data);

    /**
     * Gets the EntityType.
     *
     * @return Raw EntityType of this EntityTypeExtended
     */
    EntityType getType();

    @Override
    default boolean isExtended() { return true; }
}
