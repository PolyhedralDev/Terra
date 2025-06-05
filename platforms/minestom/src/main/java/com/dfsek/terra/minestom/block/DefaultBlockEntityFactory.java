package com.dfsek.terra.minestom.block;

import com.dfsek.terra.api.block.entity.BlockEntity;
import com.dfsek.terra.minestom.api.BlockEntityFactory;

import net.minestom.server.coordinate.BlockVec;
import net.minestom.server.instance.Instance;


public class DefaultBlockEntityFactory implements BlockEntityFactory {
    private final Instance instance;

    public DefaultBlockEntityFactory(Instance instance) {
        this.instance = instance;
    }

    @Override
    public BlockEntity createBlockEntity(BlockVec position) {
        return new MinestomBlockEntity(instance, position);
    }
}
