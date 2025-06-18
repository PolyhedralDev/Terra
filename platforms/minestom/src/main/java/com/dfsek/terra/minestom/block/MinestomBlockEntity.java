package com.dfsek.terra.minestom.block;

import com.dfsek.seismic.type.vector.Vector3;
import net.minestom.server.coordinate.BlockVec;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;

import com.dfsek.terra.api.block.entity.BlockEntity;
import com.dfsek.terra.api.block.state.BlockState;


public class MinestomBlockEntity implements BlockEntity {
    private final Instance instance;
    private final BlockVec position;
    private final Vector3 positionVec;

    public MinestomBlockEntity(Instance instance, BlockVec position) {
        this.instance = instance;
        this.position = position;
        this.positionVec = Vector3.of(position.blockX(), position.blockY(), position.blockZ());
    }

    @Override
    public boolean update(boolean applyPhysics) {
        return false;
    }

    @Override
    public Vector3 getPosition() {
        return positionVec;
    }

    @Override
    public int getX() {
        return position.blockX();
    }

    @Override
    public int getY() {
        return position.blockY();
    }

    @Override
    public int getZ() {
        return position.blockZ();
    }

    @Override
    public BlockState getBlockState() {
        return new MinestomBlockState(instance.getBlock(position));
    }

    @Override
    public Block getHandle() {
        return instance.getBlock(position);
    }
}
