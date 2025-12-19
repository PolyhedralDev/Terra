package com.dfsek.terra.allay.delegate;

import com.dfsek.seismic.type.vector.Vector3;
import org.allaymc.api.blockentity.BlockEntity;

import com.dfsek.terra.allay.Mapping;
import com.dfsek.terra.api.block.state.BlockState;


/**
 * @author daoge_cmd
 */
public record AllayBlockEntity(BlockEntity allayBlockEntity) implements com.dfsek.terra.api.block.entity.BlockEntity {

    @Override
    public boolean update(boolean applyPhysics) {
        return false;
    }

    @Override
    public Vector3 getPosition() {
        var pos = this.allayBlockEntity.getPosition();
        return Vector3.of(pos.x(), pos.y(), pos.z());
    }

    @Override
    public int getX() {
        return this.allayBlockEntity.getPosition().x();
    }

    @Override
    public int getY() {
        return this.allayBlockEntity.getPosition().y();
    }

    @Override
    public int getZ() {
        return this.allayBlockEntity.getPosition().z();
    }

    @Override
    public BlockState getBlockState() {
        var allayBlockState = this.allayBlockEntity.getBlockState();
        return new AllayBlockState(allayBlockState, Mapping.blockStateBeToJe(this.allayBlockEntity.getBlockState()));
    }

    @Override
    public Object getHandle() {
        return this.allayBlockEntity;
    }
}
