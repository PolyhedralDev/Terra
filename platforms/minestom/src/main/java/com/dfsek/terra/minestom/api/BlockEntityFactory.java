package com.dfsek.terra.minestom.api;

import com.dfsek.terra.api.block.entity.BlockEntity;

import net.minestom.server.coordinate.BlockVec;
import org.jetbrains.annotations.Nullable;


/**
 * Represents a factory interface for creating instances of BlockEntity
 * at a specified BlockVec position. This is not implemented directly because
 * Minestom does not define a way to build block entities out of the box.
 */
public interface BlockEntityFactory {
    @Nullable BlockEntity createBlockEntity(BlockVec position);
}
