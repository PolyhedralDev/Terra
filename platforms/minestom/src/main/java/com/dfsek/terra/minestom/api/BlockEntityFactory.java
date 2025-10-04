package com.dfsek.terra.minestom.api;

import net.minestom.server.coordinate.BlockVec;
import org.jetbrains.annotations.Nullable;

import com.dfsek.terra.api.block.entity.BlockEntity;


/**
 * Represents a factory interface for creating instances of BlockEntity
 * at a specified BlockVec position. For more fine-grained control, users
 * may supply their own version of this interface.
 */
public interface BlockEntityFactory {
    @Nullable BlockEntity createBlockEntity(BlockVec position);
}
